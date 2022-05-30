package com.linkpcom.mitrafast.MVVM.Client.OrderDetails;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.DATE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.RATE_AGENT;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.OrderContentsAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ClientTicketDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Client;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Chat.ChatActivity;
import com.linkpcom.mitrafast.MVVM.Client.BillDetails.BillDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Client.RateAgent.RateAgentActivity;
import com.linkpcom.mitrafast.MVVM.Client.Tracking.TrackingActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityClientOrderDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class OrderDetailsActivity extends BaseActivity {
    public static final int POSTION_NOT_SET = -1;
    public static final int IS_ORDER_FINISHED = 7;
    private ActivityClientOrderDetailsBinding mBinding;
    private OrderDetailsViewModel mViewModel;


    int orderId;
    Order mOrder;
    OrderContentsAdapter orderContentsAdapter;

    ClientTicketDialog clientTicketDialog;
    ConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getIntExtra(ID, POSTION_NOT_SET);
        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getReportOrder().observe(this, this::onReportResponse);

        mViewModel.getOrderStatusMutableLiveData().observe(this, this::onOrderStatusResponse);
        mViewModel.getOrderDetailsResponseMutableLiveData().observe(this, this::onOrderDetailsResponse);

        //New Instances
        confirmDialog = new ConfirmDialog(this);
        orderContentsAdapter = new OrderContentsAdapter();
        mBinding.orderContents.setAdapter(orderContentsAdapter);
        mViewModel.requestOrderDetails(orderId);

        mBinding.chatToAgent.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(OBJECT, mOrder);
            startActivity(intent);
        });

        mBinding.call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + "+966" + mOrder.getDriver().getMobile()));
            startActivity(callIntent);
        });
        mBinding.ivTracking.setOnClickListener(this::onTrackClick);
        mBinding.btnBill.setOnClickListener(this::onBillClick);
        mBinding.btnReport.setOnClickListener(this::StartReport);

        clientTicketDialog = new ClientTicketDialog(this);
    }

    private void onReportResponse(BaseResponse baseResponse) {
        Toast.makeText(this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void StartReport(View view) {
        if (mOrder != null) {
            clientTicketDialog.setData(mOrder.getId(), mOrder.getUser().getName().trim(), mOrder.getUser().getAddress(), mOrder.getUser().getMobile());
            clientTicketDialog.show();
        }
    }

    private void onBillClick(View view) {
        Intent intent = new Intent(this, BillDetailsActivity.class);
        intent.putExtra(OBJECT, mOrder);
        startActivity(intent);
    }

    private void onTrackClick(View view) {
        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra(OBJECT, mOrder);
        startActivity(intent);
    }

    private void onWhatsAppClick(View view) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mOrder.getDriver().getMobile()) + "@s.whatsapp.net");
            startActivity(sendIntent);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void onOrderDetailsResponse(OrderDetailsResponse response) {

        initTrackingOrRate(response);

        mOrder = response.getData();
        clientTicketDialog.setOnConfirmClicked(dialog -> {
            Client user = mOrder.getUser();
            if (clientTicketDialog.getMessage() != null)
                mViewModel.requestReportOrder(
                        AddTicketRequest.builder()
                                .mobile(user.getMobile())
                                .email("kimo@hamed.com")
                                .name(user.getName())
                                .support_section_id(1)
                                .message(clientTicketDialog.getMessage())
                                .build()
                );
            clientTicketDialog.dismiss();
        });
        Picasso.get().load(mOrder.getShop().getImage()).placeholder(R.drawable.logo).error(R.drawable.logo).into(mBinding.ivShop);
        mBinding.tvName.setText(mOrder.getShop().getName());
        mBinding.tvOrderStatus.setText(mOrder.getOrder_status().getMessage());
        mBinding.orderId.setText(String.format("#%s", mOrder.getId()));
        mBinding.paymentMethod.setText(mOrder.getPayment_type().getName());
        orderContentsAdapter.setData(mOrder.getProducts());
        mBinding.productsCost.setText(String.format("%s %s", mOrder.getShop_products_cost(), mOrder.getCurrency()));
        double cost = Double.parseDouble(mOrder.getDelivery_cost_with_tax());
        mBinding.deliveryCost.setText(String.format(Locale.US, "%.2f %s", cost, mOrder.getCurrency()));

        double totalCost = Double.parseDouble(mOrder.getTotal_cost());
        mBinding.totalCost.setText(String.format(Locale.US, "%.2f %s", totalCost, mOrder.getCurrency()));

        if (mOrder.getCoupon_discount_cost() != null && !mOrder.getCoupon_discount_cost().equals("0.00")) {
            mBinding.couponDiscount.setText(String.format("%s %s", mOrder.getCoupon_discount_cost(), mOrder.getCurrency()));


            if (mOrder.getCoupon_discount_percentage() != null && !mOrder.getCoupon_discount_percentage().equals("0"))
                mBinding.couponDiscountLabel.setText(String.format("%s: %s %%", getString(R.string.discount_rate), mOrder.getCoupon_discount_percentage()));
            else {
                mBinding.layoutCoupon.setVisibility(View.GONE);
            }
        } else {
            mBinding.couponDiscount.setVisibility(View.GONE);
            mBinding.couponDiscountLabel.setVisibility(View.GONE);
        }

        if (mOrder.getDriver() != null) {
            isShowContactDriver(true);
            mBinding.tvNameAgent.setText(mOrder.getDriver().getName());
            Picasso.get().load(mOrder.getDriver().getImage()).error(R.drawable.logo).placeholder(R.drawable.logo).into(mBinding.ivAgent);
        } else {
            isShowContactDriver(false);
        }

        mBinding.btnRate.setOnClickListener(v -> whenClickRateAgent(response));
    }

    private void whenClickRateAgent(OrderDetailsResponse response) {
        Intent intent = new Intent(this, RateAgentActivity.class);
        intent.putExtra(ID, response.getData().getId());
        intent.putExtra(DATE, response.getData());
        finish();
        startActivity(intent);
    }

    private void initTrackingOrRate(OrderDetailsResponse response) {

        if (response.getData().getOrder_status().getId() == IS_ORDER_FINISHED) {
            mBinding.llOptionsContainer.setVisibility(View.GONE);
            mBinding.ivTracking.setVisibility(View.GONE);

            if (response.getData().getRate().getClient_rate() == null)
                mBinding.btnRate.setVisibility(View.VISIBLE);
            else
                mBinding.btnRate.setVisibility(View.GONE);


        } else {
            mViewModel.startOrderStatusListening(orderId);
            mBinding.ivTracking.setVisibility(View.VISIBLE);
        }
    }

    private void isShowContactDriver(boolean status) {
        if (status) {
            mBinding.chatToAgent.setVisibility(View.VISIBLE);
            mBinding.ivAgent.setVisibility(View.VISIBLE);
            mBinding.tvNameAgent.setVisibility(View.VISIBLE);
            mBinding.call.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivAgent.setVisibility(View.GONE);
            mBinding.chatToAgent.setVisibility(View.GONE);
            mBinding.tvNameAgent.setVisibility(View.GONE);
            mBinding.call.setVisibility(View.GONE);
        }
    }

    private void onOrderStatusResponse(String orderStatus) {
        mBinding.tvOrderStatus.setText(mOrder.getOrder_status().getMessage());
        switch (orderStatus) {
            case "3":
            case "4":
            case "7":
            case "6":
            case "5":
                break;
        }
    }

}