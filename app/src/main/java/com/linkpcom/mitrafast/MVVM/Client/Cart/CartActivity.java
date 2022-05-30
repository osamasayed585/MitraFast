package com.linkpcom.mitrafast.MVVM.Client.Cart;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NAME_CARD;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.SELECT_PAYMENT_METHOD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.CartProductsAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Coupon;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentMethod;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MakeOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Locating.LocatingActivity;
import com.linkpcom.mitrafast.MVVM.Client.PaymentMethods.PaymentMethodsActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityCartBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class CartActivity extends BaseActivity implements CartProductsAdapter.CartProductsAdapterOnClickHandler {
    ActivityCartBinding mBinding;


    MakeOrderRequest request;
    List<CartProduct> cartProducts;

    float productsTotalCost;
    float couponDiscountCost;


    ConfirmDialog confirmDialog;

    PaymentMethod selectedPaymentMethod;

    String currency;
    Order order;

    private CartProductsAdapter cartProductsAdapter;
    private CartViewModel mViewModel;
    private Coupon coupon;
    private float totalCost;
    private PaymentCard paymentCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getMakeOrderResponseMutableLiveData().observe(this, this::onMakeOrderResponse);

        mViewModel.getAllCartProductsResponseMutableLiveData().observe(this, this::onAllCartProductsResponse);
        mViewModel.getTotalCostResponseMutableLiveData().observe(this, this::onTotalCostResponse);
        mViewModel.getItemDeletedResponseMutableLiveData().observe(this, this::onItemDeletedResponse);
        mViewModel.getItemUpdatedResponseMutableLiveData().observe(this, this::onItemUpdatedResponse);
        mViewModel.getCouponResponseMutableLiveData().observe(this, this::onCouponResponse);
        mViewModel.getPaymentResponseMutableLiveData().observe(this, this::onPaymentResponse);


        //New Instances
        cartProductsAdapter = new CartProductsAdapter();
        confirmDialog = new ConfirmDialog(this);
        request = new MakeOrderRequest();


        mBinding.deliveryLocation.setText(String.format("%s(%s)", preferencesManager.getAddressName(), preferencesManager.getAddress()));


        mBinding.cartProducts.setAdapter(cartProductsAdapter);
        //ClickListeners
        cartProductsAdapter.setDeleteClickListener(this);

        mBinding.confirm.setOnClickListener(this::onConfirmClick);
        mBinding.paymentMethod.setOnClickListener(this::onSelectPaymentMethodClick);
        mBinding.applyCoupon.setOnClickListener(this::onApplyCouponClick);


        selectedPaymentMethod = PaymentMethod.builder().id(5).name(getString(R.string.wallet)).build();
        mBinding.paymentMethod.setText(selectedPaymentMethod.getName());
        mViewModel.requestCart();
    }

    private void onCouponResponse(CouponResponse response) {
        if (response.getData().getVoucher().getType().getId() == 1) {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            mBinding.coupon.setText("");
            coupon = response.getData();
            mViewModel.requestCart();
        } else {
            Toast.makeText(this, getString(R.string.the_coupon_balance_has_been_added_to_the_wallet), Toast.LENGTH_SHORT).show();
        }

    }

    private void onApplyCouponClick(View view) {
        if (!isLoggedIn())
            return;

        if (!mBinding.coupon.getText().toString().trim().equals(""))
            mViewModel.requestCoupon(mBinding.coupon.getText().toString());

    }


    private void onMakeOrderResponse(OrderDetailsResponse response) {

        MainApplication.setPaymentSuccess(false);
        order = response.getData();
        mViewModel.requestDeleteCartItems().observe(this, this::onDeleteCartItemsResponse);

    }

    private void onDeleteCartItemsResponse(Boolean aBoolean) {
        Intent intent = new Intent(this, LocatingActivity.class);
        intent.putExtra(OBJECT, order);
        finish();
        startActivity(intent);

    }

    private void onTotalCostResponse(Float productsTotalCost) {
        float deliveryCostVal = Float.parseFloat(cartProducts.get(0).getDelivery_cost());
        mBinding.productsCost.setText(String.format("%s %s", productsTotalCost, currency));
        mBinding.deliveryCost.setText(String.format("%s %s", cartProducts.get(0).getDelivery_cost(), currency));

        if (coupon != null) {
            float discountVal = calculateCouponDiscountCost(deliveryCostVal);
            mBinding.couponDiscountLabel.setText(String.format("%s: %s%%", getString(R.string.discount_rate), coupon.getCoupon_discount()));
            deliveryCostVal = deliveryCostVal - discountVal;
            mBinding.deliveryCost.setText(String.format("%s %s", deliveryCostVal, currency));

            mBinding.couponDiscount.setVisibility(View.VISIBLE);
            mBinding.couponDiscountLabel.setVisibility(View.VISIBLE);
            mBinding.couponDiscount.setText(couponDiscountCost != 0 ? String.valueOf(couponDiscountCost) : getString(R.string.discount_on_delivery_cost));
        } else {
            mBinding.couponDiscount.setVisibility(View.GONE);
            mBinding.couponDiscountLabel.setVisibility(View.GONE);
        }

//        totalCost = productsTotalCost + Float.parseFloat(cartProducts.get(0).getDelivery_cost()) - couponDiscountCost;
        float TotalCostVal = productsTotalCost + deliveryCostVal;
        mBinding.totalCost.setText(String.format("%s %s", TotalCostVal, currency));
//        mBinding.totalCost.setText(String.format("%s %s", totalCost, currency));
        this.productsTotalCost = TotalCostVal;
    }


    private float calculateCouponDiscountCost(Float deliveryCost) {
        float result = 0;
//        TODO voucher

        if (coupon.getVoucher().getShop_id() != 0) {
            float discount = Float.parseFloat(coupon.getVoucher().getDiscount());
            boolean isFixed = coupon.getVoucher().is_fixed();

            float discountValue = (deliveryCost * (discount / 100));

            if (isFixed) {
                float maxDiscount= coupon.getVoucher().getMaximum_discount();
                result = Math.min(discountValue, maxDiscount);
            } else {
                result = discountValue;
            }

        }
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (MainApplication.isPaymentSuccess()) {
            mViewModel.requestMakeOrder(getData());
        }
    }


    private void onConfirmClick(View view) {
        if (!isLoggedIn())
            return;
        if (!validation())
            return;

        if (selectedPaymentMethod.getId() == 1 || selectedPaymentMethod.getId() == 5) {
            mViewModel.requestMakeOrder(getData());
        } else {
            if (paymentCard != null)
                mViewModel.requestPayment(PaymentRequest.builder()
                        .credit_card_id(String.valueOf(paymentCard.getId()))
                        .payment_type("creditcard")
                        .redirect_url("app://mitrafast/makeOrder")
                        .price(totalCost)
                        .build());
            else {
                mViewModel.requestPayment(PaymentRequest.builder()
                        .payment_type("creditcard")
                        .redirect_url("app://mitrafast/makeOrder")
                        .price(totalCost)
                        .build());
            }
        }
    }

    private void onPaymentResponse(PaymentResponse response) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(response.getData().getUrl()));
        startActivity(intent);
    }

    private MakeOrderRequest getData() {
        request.setShop_id(cartProducts.get(0).getShopId());
        request.setCredit_card_id(paymentCard != null ? String.valueOf(paymentCard.getId()) : "");
        request.setPayment_id(selectedPaymentMethod.getId());
        request.setOrderProducts(cartProducts);
        request.setLocation_id(preferencesManager.getAddressId());
        request.setNotes(mBinding.shopNotes.getText().toString());
        request.setCoupon_id(coupon != null ? String.valueOf(coupon.getVoucher().getId()) : null);

        return request;
    }

    private boolean validation() {


        return
                validator.isGraterThan(productsTotalCost - couponDiscountCost, Float.parseFloat(cartProducts.get(0).getMin_value())) &&
                        validator.isSmallerThan(productsTotalCost - couponDiscountCost, Float.parseFloat(cartProducts.get(0).getMax_value())) &&
                        validator.isNotNull(preferencesManager.getAddressId(), getString(R.string.select_delivery_location)) &&
                        validator.isNotNull(selectedPaymentMethod, getString(R.string.select_payment_method));


    }


    private void onAllCartProductsResponse(List<CartProduct> cartProducts) {
        //Set List Again
        this.cartProducts = cartProducts;

        cartProductsAdapter.setData(cartProducts);

        if (!cartProducts.isEmpty()) {
            currency = cartProducts.get(0).getCurrency();
            mViewModel.requestTotalCost();
        }

        mBinding.container.setVisibility(!cartProducts.isEmpty() ? View.VISIBLE : View.GONE);
        mBinding.cartIsEmpty.setVisibility(!cartProducts.isEmpty() ? View.GONE : View.VISIBLE);


        if (confirmDialog.getDialog().isShowing())
            confirmDialog.getDialog().dismiss();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PAYMENT_METHOD:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        selectedPaymentMethod = data.getParcelableExtra(OBJECT);

                        if (data.getParcelableExtra(NAME_CARD) != null) {
                            paymentCard = data.getParcelableExtra(NAME_CARD);
                            mBinding.paymentMethod.setText(String.format("%s / %s", selectedPaymentMethod.getName(), paymentCard.getCredit_card_name()));
                            break;
                        }
                        mBinding.paymentMethod.setText(selectedPaymentMethod.getName());
                    }
                }
                break;
        }
    }

    @Override
    public void onProductDeleteClick(int productId) {
        confirmDialog.setTitle(getString(R.string.are_you_sure_you_want_to_delete_this_product));
        confirmDialog.setConfirmListDialogHandler(() -> {
            //Delete From Cart
            mViewModel.requestDeleteProductById(productId);
        });

        if (!confirmDialog.getDialog().isShowing())
            confirmDialog.getDialog().show();
    }

    private void onItemDeletedResponse(Boolean isDeleted) {
        mViewModel.requestCart();
    }

    @Override
    public void onPlusOrMinusClick(int productId, int currentCount) {
        mViewModel.requestUpdateById(productId, currentCount);
    }

    private void onItemUpdatedResponse(Boolean isUpdated) {
        mViewModel.requestCart();

    }


    private void onSelectPaymentMethodClick(View view) {
        if (!isLoggedIn())
            return;
        Intent intent = new Intent(this, PaymentMethodsActivity.class);
        startActivityForResult(intent, SELECT_PAYMENT_METHOD);

    }

}
