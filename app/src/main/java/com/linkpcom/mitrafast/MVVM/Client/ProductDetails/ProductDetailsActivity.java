package com.linkpcom.mitrafast.MVVM.Client.ProductDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.linkpcom.mitrafast.Classes.Adapters.ChoicesAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.ExtrasAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.SizesAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.TypesAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Choice;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Product;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Size;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Type;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SameProductRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductDetailsResponse;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Cart.CartActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.PRODUCT_ID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsActivity extends BaseActivity {

    ActivityProductDetailsBinding mBinding;
    ProductDetailsViewModel mViewModel;

    private Shop shop;
    private Product product;
    private ConfirmDialog anotherStoreDialog;
    private TypesAdapter typesAdapter;
    private Type selectedType;
    private SizesAdapter sizesAdapter;
    private Size selectedSize;
    private ExtrasAdapter extrasAdapter;
    private ChoicesAdapter choicesAdapter;
    private int productId;
    private double selectedExtrasCost;
    private double selectedChoicesCost;
    private double totalCost;
    private String currency;
    private Choice selectedChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        productId = getIntent().getIntExtra(PRODUCT_ID, -1);


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        //Request And Observer

        mViewModel.getProductDetailsResponseMutableLiveData().observe(this, this::onProductDetailsResponse);
        mViewModel.getFindSameProductResponseMutableLiveData().observe(this, this::onFindSameProductResponse);
        mViewModel.getIsAnotherStoreExistResponseMutableLiveData().observe(this, this::onIsAnotherStoreExistResponse);
        mViewModel.getAddProductResponseMutableLiveData().observe(this, this::onAddProductResponse);
        mViewModel.getUpdateCountByIdResponseMutableLiveData().observe(this, this::onUpdateCountByIdResponse);
        mViewModel.getDeleteCartItemsMutableLiveData().observe(this, this::onDeleteCartItemsResponse);
        mViewModel.getAllCartProductsResponseMutableLiveData().observe(this, this::onAllCartProductsResponse);
        mViewModel.getTotalCostResponseMutableLiveData().observe(this, this::onTotalCostResponse);

        //New Instances
        anotherStoreDialog = new ConfirmDialog(this);
        extrasAdapter = new ExtrasAdapter();
        choicesAdapter = new ChoicesAdapter();
        typesAdapter = new TypesAdapter();
        sizesAdapter = new SizesAdapter();


        extrasAdapter.setItemClickListener(this::onExtraItemClick);
        choicesAdapter.setItemClickListener(this::onChoiceItemClick);
        typesAdapter.setItemClickListener(this::onTypeItemClick);
        sizesAdapter.setItemClickListener(this::onSizeItemClick);


        mBinding.extras.setAdapter(extrasAdapter);
        mBinding.choices.setAdapter(choicesAdapter);
        mBinding.types.setAdapter(typesAdapter);
        mBinding.sizes.setAdapter(sizesAdapter);
        choicesAdapter.getPrice().subscribe(price -> {
            selectedChoicesCost = price;
        });


        mBinding.btnAddToCart.setOnClickListener(this::onAddToCartClick);


        prepareAnotherStoreDialog();

        mViewModel.requestProductDetails(ProductDetailsRequest.builder()
                .product_id(productId)
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .build());
        mBinding.plus.setOnClickListener(this::onPlusClick);
        mBinding.minus.setOnClickListener(this::onMinusClick);

        mBinding.llContinueOrder.setOnClickListener(this::onContinueOrderClick);

        mBinding.ivShare.setOnClickListener(this::onShareClick);


    }


    private void onShareClick(View view) {
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix(getString(R.string.dynamic_links_uri_prefix))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setMinimumVersion(0)
                        .build())
                .setLink(Uri.parse(getString(R.string.dynamic_links_uri_prefix) + "?type=product&id=" + product.getId()));
        builder.buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            share(shortLink.toString());
                        } else {
                            // Error
                            // ...
                        }
                    }
                });
    }

    private void share(String deepLink) {
        Picasso.get().load(product.getImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.i_advice_you_with) + " " + product.getProduct_name() + "\n" + deepLink + "\n" + getString(R.string.in_mitrafast_app));
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(bitmap));
                startActivity(Intent.createChooser(intent, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    private Uri getBitmapFromView(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void onContinueOrderClick(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.requestCart();
    }

    private void onAllCartProductsResponse(List<CartProduct> cartProducts) {
        if (cartProducts.isEmpty()) {
            mBinding.llCartContainer.setVisibility(View.GONE);
        } else {
            mBinding.llCartContainer.setVisibility(View.VISIBLE);
            currency = cartProducts.get(0).getCurrency();

            int count = 0;
            for (int i = 0; i < cartProducts.size(); i++) {
                count += cartProducts.get(i).getQuantity();
            }
            mBinding.tvOrdersNumber.setText(String.valueOf(count));
            mViewModel.requestTotalCost();

        }

    }

    private void onTotalCostResponse(Float total) {
        mBinding.tvOrdersTotal.setText(String.format("%s %s", total, currency));
    }

    private void onPlusClick(View view) {
        mBinding.count.setText(String.valueOf(Integer.parseInt(mBinding.count.getText().toString()) + 1));
        calculateTotalCost();
    }

    private void onMinusClick(View view) {
        if (Integer.parseInt(mBinding.count.getText().toString()) > 1) {
            mBinding.count.setText(String.valueOf(Integer.parseInt(mBinding.count.getText().toString()) - 1));
            calculateTotalCost();

        }
    }

    private void prepareAnotherStoreDialog() {
        anotherStoreDialog.setTitle(getString(R.string.this_product_is_from_another_merchant));
        anotherStoreDialog.setConfirmListDialogHandler(() -> {
            mViewModel.requestDeleteCartItems();
        });

    }

    private void onFindSameProductResponse(List<CartProduct> cartProducts) {
        if (!cartProducts.isEmpty()) {
            Timber.d("Shady onFindSameProductResponse: ");
            mViewModel.requestUpdateById(cartProducts.get(0).getId(), cartProducts.get(0).getQuantity() + Integer.parseInt(mBinding.count.getText().toString()));
        } else {
            addProductToCart();
        }
    }

    private void addProductToCart() {
        mViewModel.requestAddToCart(CartProduct.builder()
                .product_id(product.getId())
                .name(product.getProduct_name())
                .image(product.getImage())
                .cost(totalCost)
                .quantity(Integer.parseInt(mBinding.count.getText().toString()))
                .shopId(shop.getId())
                .currency(product.getCurrency())
                .type_id(selectedType != null ? selectedType.getId() : 0)
                .type_name(selectedType != null ? selectedType.getName() : null)
                .size_id(selectedSize != null ? selectedSize.getId() : 0)
                .size_name(selectedSize != null ? selectedSize.getName() : null)
                .extras_ids(extrasAdapter.getSelectedDataIdes().toString())
                .extras_names(extrasAdapter.getSelectedDataNames().toString())
                .choices_ids(choicesAdapter.getSelectedDataIdes().toString())
                .choices_names(choicesAdapter.getSelectedDataNames().toString())
                .min_value(shop.getMin_order_value())
                .max_value(shop.getMax_order_value())
                .distance_text(shop.getDistance_text())
                .distance_value(shop.getDistance_value())
                .duration_text(shop.getDuration_text())
                .duration_value(shop.getDuration_value())
                .delivery_cost(String.valueOf(shop.getDelivery_cost_with_tax()))
                .build());
    }

    private void onAddProductResponse(Long aLong) {
        startActivity(new Intent(this, CartActivity.class));

    }

    private void onUpdateCountByIdResponse(Integer integer) {
        startActivity(new Intent(this, CartActivity.class));
    }

    private void onIsAnotherStoreExistResponse(List<CartProduct> cartProducts) {
        if (cartProducts.isEmpty())
            mViewModel.requestFindSameProduct(SameProductRequest.builder()
                    .product_id(product.getId())
                    .type_id(selectedType != null ? String.valueOf(selectedType.getId()) : "0.0")
                    .size_id(selectedSize != null ? String.valueOf(selectedSize.getId()) : "0.0")
                    .extras_ids(extrasAdapter.getSelectedDataIdes().toString())
                    .choices_ids(choicesAdapter.getSelectedDataIdes().toString())
                    .build());
        else
            anotherStoreDialog.getDialog().show();

    }

    private void onDeleteCartItemsResponse(Boolean isDeleted) {
        if (isDeleted)
            addProductToCart();
    }

    private void onProductDetailsResponse(ProductDetailsResponse response) {
        product = response.getData();
        shop = product.getShop();

        Picasso.get().load(product.getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.productImage);
        mBinding.calories.setText(product.getCal());
        mBinding.tvName.setText(product.getProduct_name());
        mBinding.description.setText(product.getDescription());

        mBinding.extrasLabel.setVisibility(product.getExtras().isEmpty() ? View.GONE : View.VISIBLE);
        mBinding.choicesLabel.setVisibility(product.getChoices().isEmpty() ? View.GONE : View.VISIBLE);

        typesAdapter.setCurrency(product.getCurrency());

        typesAdapter.setData(product.getTypes());
        if (product.getTypes() != null && !product.getTypes().isEmpty()) {
            mBinding.textType.setVisibility(View.VISIBLE);
            mBinding.separateType.setVisibility(View.VISIBLE);
        }

        sizesAdapter.setCurrency(product.getCurrency());
        sizesAdapter.setData(product.getSizes());
        if (product.getSizes() != null && !product.getSizes().isEmpty()) {
            mBinding.textSize.setVisibility(View.VISIBLE);
            selectedSize = product.getSizes().get(0);
        }
        if (product.getChoices() != null && !product.getChoices().isEmpty()) {
            selectedChoice = product.getChoices().get(0);
        }


        extrasAdapter.setCurrency(product.getCurrency());
        extrasAdapter.setData(product.getExtras());
        choicesAdapter.setCurrency(product.getCurrency());
        choicesAdapter.setData(product.getChoices());


        mBinding.extrasSeparator.setVisibility(extrasAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
        mBinding.choicesSeparator.setVisibility(choicesAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);

        calculateTotalCost();

    }

    private void calculateTotalCost() {
        if (product.getTypes().size() == 0
                && product.getSizes().size() == 0
                && product.getExtras().size() == 0
                && product.getChoices().size() == 0) {
            totalCost = product.getPrice();
        } else {
            totalCost = (selectedSize != null ? selectedSize.getPrice() : 0.0) + (selectedType != null ? selectedType.getPrice() : 0.0) + selectedExtrasCost + selectedChoicesCost;
        }
        mBinding.totalCost.setText(String.format("%s%s", totalCost * Integer.parseInt(mBinding.count.getText().toString()), product.getCurrency()));
    }

    private void onChoiceItemClick(double totalChoicesCost) {
        selectedChoicesCost = totalChoicesCost;
        calculateTotalCost();
    }

    private void onExtraItemClick(double totalExtrasCost) {
        selectedExtrasCost = totalExtrasCost;
        calculateTotalCost();
    }

    private void onSizeItemClick(Size size) {
        selectedSize = size;
        calculateTotalCost();
    }

    private void onTypeItemClick(Type type) {

        selectedType = type;
        calculateTotalCost();
    }

    private void onAddToCartClick(View view) {
        if (product == null || !shop.is_working()) {
            Snackbar.make(view, getResources().getString(R.string.isBusy), BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }
        if (!validation())
            return;
        mViewModel.requestIsAnotherStoreExist(shop.getId());

    }

    private boolean validation() {

        if (extrasAdapter.getSelectedDataIdes().size() < product.getExtra_min() || extrasAdapter.getSelectedDataIdes().size() > product.getExtra_max()) {
            Toast.makeText(this, String.format("%s %s %s %s", getString(R.string.minimum_extras_count_is), product.getExtra_min(), getString(R.string.and_maximum), product.getExtra_max()), Toast.LENGTH_LONG).show();
            return false;
        }
        if (choicesAdapter.getSelectedDataIdes().size() < product.getChoice_min() || choicesAdapter.getSelectedDataIdes().size() > product.getChoice_max()) {
            Toast.makeText(this, String.format("%s %s %s %s", getString(R.string.minimum_choices_count_is), product.getChoice_min(), getString(R.string.and_maximum), product.getChoice_max()), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

}


