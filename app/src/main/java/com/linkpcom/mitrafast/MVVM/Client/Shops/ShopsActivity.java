package com.linkpcom.mitrafast.MVVM.Client.Shops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.ShopsAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.SubCategoriesAdapter;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityShopsBinding;

import java.util.ArrayList;
import java.util.List;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint
public class ShopsActivity extends BaseActivity {

    private ActivityShopsBinding mBinding;
    private ShopsViewModel mViewModel;
    private ShopsAdapter shopsAdapter;
    private int categoryId;
    private String categoryName;
    private String keyWard;
    private SubCategoriesAdapter subCategoriesAdapter;
    private int subCategoryId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShopsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //Intent
        categoryId = getIntent().getIntExtra(CONSTANTS.INTENTS.ID, -1);
        categoryName = getIntent().getStringExtra(CONSTANTS.INTENTS.NAME);

        mBinding.tvLocation.setText(categoryName);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ShopsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getShopsResponseMutableLiveData().observe(this, this::onShopsResponse);
        mViewModel.getSubCategoriesResponseMutableLiveData().observe(this, this::onSubCategoriesResponse);

        //New Instances
        shopsAdapter = new ShopsAdapter();
        subCategoriesAdapter = new SubCategoriesAdapter();

        shopsAdapter.setItemClickListener(this::onShopClick);
        shopsAdapter.setPreferencesManager(preferencesManager);
        compositeDisposable.add(shopsAdapter.isFavoriteClicked.subscribe(this::onFavoriteClicked));

        subCategoriesAdapter.setItemClickListener(this::onSubCategoryClick);


        mBinding.shops.setAdapter(shopsAdapter);
        mBinding.rvSubCategories.setAdapter(subCategoriesAdapter);


        mBinding.ivSearch.setOnClickListener(this::onSearchClick);
        mBinding.etSearch.setOnEditorActionListener(this::onSearchKeyboardButtonClick);


    }

    private void onFavoriteClicked(Shop shop) {
        if (isLoggedIn()){
            if (shop.is_favourite()) mViewModel.requestRemoveFavoriteShop(shop.getId());
            else mViewModel.requestAddFavoriteShop(shop.getId());
        }
    }

    private void onSearchClick(View view) {
        search();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.tvLocation.setText(preferencesManager.getAddressName());
        mViewModel.requestSubCategories(categoryId);
    }

    private boolean onSearchKeyboardButtonClick(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    private void onSubCategoryClick(Category category) {
        subCategoryId = category.getId();
        requestShops();
    }

    private void onSubCategoriesResponse(CategoriesResponse response) {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder()
                .id(0)
                .name(getString(R.string.all))
                .build());
        categories.addAll(response.getData());
        subCategoriesAdapter.setData(categories);

        //0 For All Category
        subCategoryId = 0;
        requestShops();
    }

    private void search() {
        keyWard = mBinding.etSearch.getText().toString();
        if (!keyWard.trim().equals("") || keyWard != null) {
            requestShops();
        }
    }

    private void requestShops() {
        mViewModel.requestShops(ShopsRequest.builder()
                .category_id(categoryId)
                .classification_id(subCategoryId)
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .keyword(keyWard)
                .build());
    }

    private void onShopsResponse(ShopsResponse response) {
        shopsAdapter.setData(response.getData());
    }

    private void onShopClick(Shop shop) {
        startActivity(new Intent(this, ShopDetailsActivity.class).putExtra(OBJECT, shop));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}