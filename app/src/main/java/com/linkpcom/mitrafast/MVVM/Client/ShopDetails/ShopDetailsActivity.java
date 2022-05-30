package com.linkpcom.mitrafast.MVVM.Client.ShopDetails;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.linkpcom.mitrafast.Classes.Adapters.ProductsAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.ShopCategoriesAdapter;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.UI.EndlessRecyclerViewScrollListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Product;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductCountResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopDetailsResponse;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Cart.CartActivity;
import com.linkpcom.mitrafast.MVVM.Client.ProductDetails.ProductDetailsActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityShopDetailsBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ShopDetailsActivity extends BaseActivity {

    private ActivityShopDetailsBinding mBinding;
    ShopDetailsViewModel mViewModel;
    ProductsAdapter productsAdapter;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    GridLayoutManager gridLayoutManager;
    ShopCategoriesAdapter shopCategoriesAdapter;
    Shop shop;
    String keyWard;
    int shopCategoryId;
    private String currency;
    private List<Product> products;
    private int count;
    private int shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShopDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ShopDetailsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getProductsResponseMutableLiveData().observe(this, this::onProductsResponse);
        mViewModel.getShopCategoriesResponseMutableLiveData().observe(this, this::onShopCategoriesResponse);
        mViewModel.getAllCartProductsResponseMutableLiveData().observe(this, this::onAllCartProductsResponse);
        mViewModel.getTotalCostResponseMutableLiveData().observe(this, this::onTotalCostResponse);
        mViewModel.getProductCountResponseMutableLiveData().observe(this, this::onProductCountResponse);
        mViewModel.getShopDetailsResponseMutableLiveData().observe(this, this::onShopDetailsResponse);

        //Intent
        shop = getIntent().getParcelableExtra(OBJECT);
        if (shop != null)
            mBinding.tvTitle.setText("Test");
        else {
            shop_id = getIntent().getIntExtra(ID, 1);
            Timber.d("hi karim we are here");
        }
        //New Instances
        productsAdapter = new ProductsAdapter();
        shopCategoriesAdapter = new ShopCategoriesAdapter();
        gridLayoutManager = new GridLayoutManager(this, 2);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                mViewModel.requestProducts(ProductsRequest.builder()
//                        .shop_id(shop.getId())
//                        .shop_category_id(shopCategoryId)
//                        .keyword(keyWard)
//                        .page(page)
//                        .build());
            }
        };


        productsAdapter.setItemClickListener(this::onProductClick);
        shopCategoriesAdapter.setItemClickListener(this::onShopCategoryClick);


        mBinding.products.setAdapter(productsAdapter);
        mBinding.products.setLayoutManager(gridLayoutManager);
        mBinding.products.addOnScrollListener(endlessRecyclerViewScrollListener);

        mBinding.shopCategories.setAdapter(shopCategoriesAdapter);


        mBinding.etSearch.setOnEditorActionListener(this::onSearchKeyboardButtonClick);
        mBinding.ivSearch.setOnClickListener(this::onSearchClick);


        mBinding.ivTwitter.setOnClickListener(this::onTwitterClick);
        mBinding.ivInstgram.setOnClickListener(this::onInstgramClick);
        mBinding.ivSnapChat.setOnClickListener(this::onSnapChatClick);
        mBinding.llContinueOrder.setOnClickListener(this::onContinueOrderClick);
        mBinding.ivShare.setOnClickListener(this::onShareClick);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

        StrictMode.setVmPolicy(builder.build());

    }

    private void onShareOptions(View view) {
//        if (mBinding.shareView.getVisibility() != View.VISIBLE) {
//            mBinding.shareView.setVisibility(View.VISIBLE);
//        } else {
//            mBinding.shareView.setVisibility(View.GONE);
//        }
    }

    private void onShareClick(View view) {
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(Uri.parse(getString(R.string.dynamic_links_uri_prefix) + "?type=shop&id=" + shop.getId()))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().setMinimumVersion(1).build())
                .setDomainUriPrefix(getString(R.string.dynamic_links_uri_prefix));
        builder.buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        share(shortLink.toString());
                    } else {
                        // Error
                        // ...
                    }
                });
    }

    private void share(String deepLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s\n%s\n%s", getString(R.string.i_advice_you_with), shop.getName(), deepLink, getString(R.string.in_mitrafast_app)));
        startActivity(Intent.createChooser(intent, "Share Shop"));
    }


    private Uri getBitmapFromView(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void onShopDetailsResponse(ShopDetailsResponse response) {
        this.shop = response.getData();
        fillShopDetails(response.getData());
    }

    private void onContinueOrderClick(View view) {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shop != null) {
            fillShopDetails(shop);
        } else {
            mViewModel.requestShopDetails(ShopDetailsRequest.builder()
                    .shop_id(getIntent().getIntExtra(ID, -1))
                    .lat(preferencesManager.getLat())
                    .lng(preferencesManager.getLng())
                    .build());
        }
        mViewModel.requestCart();
    }

    private void onAllCartProductsResponse(List<CartProduct> cartProducts) {
        if (cartProducts.isEmpty() || cartProducts.get(0).getShopId() != shop.getId()) {
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

    private void onSnapChatClick(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(shop.getSnapchat()));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void onInstgramClick(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(shop.getInstgram()));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void onTwitterClick(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(shop.getTwitter()));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean onSearchKeyboardButtonClick(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    private void onSearchClick(View view) {
        search();
    }

    private void search() {
        keyWard = mBinding.etSearch.getText().toString();
        if (!keyWard.trim().equals("") || keyWard != null)
            requestProducts();

    }

    private void onShopCategoryClick(Category category) {
        shopCategoryId = category.getId();

        requestProducts();
    }

    private void fillShopDetails(Shop shop) {

        Picasso.get().load(shop.getLogo()).error(R.drawable.logo_holder).placeholder(R.drawable.logo_holder).into(mBinding.shopImageLogo);
        mBinding.shopName.setText(shop.getName());
        mBinding.tvTitle.setText(shop.getName());
        mBinding.distance.setText(convertKmToArabic(shop.getDistance_text()));

        if (shop.is_working())
            mBinding.shopImageLogo.setBorderColor(getResources().getColor(R.color.colorAccent));
        else mBinding.shopImageLogo.setBorderColor(getResources().getColor(R.color.red));

        mBinding.tvRate.setRating(Float.parseFloat(shop.getRate()));

        mBinding.tvTime.setText(String.format("%s ", shop.getDuration_text()));

        mBinding.tvIsTraceable.setText(shop.isTraceable() ? getString(R.string.traceable) : getString(R.string.not_traceable));

        mBinding.tvMinValue.setText(String.format("%s %s", shop.getMin_order_value(), shop.getCurrency()));
        mBinding.tvMaxValue.setText(String.format("%s %s", shop.getMax_order_value(), shop.getCurrency()));
        mBinding.tvDeliveryCost.setText(String.format("%s %s", shop.getDelivery_cost_with_tax(), shop.getCurrency()));

        mViewModel.requestShopCategories(shop.getId());

    }

    // for change distance form arabic to english and vice versa
    private String convertKmToArabic(String str) {
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language.equals("ar")) {

            if (str.contains("km")) {
                String base = str.substring(0, str.length() - 2);
                return String.format("%s %s", base, "كم");
            } else return str;

        } else return str;
    }

    private void onShopCategoriesResponse(CategoriesResponse response) {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder()
                .id(0)
                .name(getString(R.string.all))
                .build());
        categories.addAll(response.getData());
        shopCategoriesAdapter.setData(categories);

        requestProducts();
    }

    private void requestProducts() {
        productsAdapter.clear();
        endlessRecyclerViewScrollListener.resetState();
        mViewModel.requestProducts(ProductsRequest.builder()
                .shop_id(shop.getId())
                .shop_category_id(shopCategoryId)
                .keyword(keyWard)
                .page(1)
                .build());
    }

    private void onProductsResponse(ProductsResponse response) {
        productsAdapter.setData(response.getData());
        products = productsAdapter.getData();

        if (productsAdapter.getData().isEmpty())
            mBinding.tvNoProducts.setVisibility(View.VISIBLE);
        else
            mBinding.tvNoProducts.setVisibility(View.GONE);

        if (!productsAdapter.getData().isEmpty()) {
            count = 0;
            mViewModel.requestProductCount(products.get(count).getId());
        }
    }

    private void onProductCountResponse(ProductCountResponse response) {
        productsAdapter.setProductCount(response);
        count++;
        if (count < products.size())
            mViewModel.requestProductCount(products.get(count).getId());
    }

    private void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(CONSTANTS.INTENTS.PRODUCT_ID, product.getId());
        intent.putExtra(OBJECT, shop);

        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}