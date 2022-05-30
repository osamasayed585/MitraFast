package com.linkpcom.mitrafast.MVVM.Client.SearchShops;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.ShopsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SearchShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivitySearchShopsBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NAME;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

@AndroidEntryPoint
public class SearchShopsActivity extends BaseActivity {

    private ActivitySearchShopsBinding mBinding;
    private SearchShopsViewModel mViewModel;
    private ShopsAdapter shopsAdapter;
    private String keyWard;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySearchShopsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(SearchShopsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getShopsResponseMutableLiveData().observe(this, this::onShopsResponse);

        //New Instances
        shopsAdapter = new ShopsAdapter();

        shopsAdapter.setItemClickListener(this::onShopClick);

        compositeDisposable.add(shopsAdapter.isFavoriteClicked.subscribe(this::OnFavoriteClick));

        shopsAdapter.setPreferencesManager(preferencesManager);

        keyWard = getIntent().getStringExtra(NAME);

        mBinding.shops.setAdapter(shopsAdapter);

        requestSearchShops();

        mBinding.ivSearch.setOnClickListener(this::onSearchClick);
        mBinding.etSearch.setText(keyWard);
        mBinding.etSearch.setOnEditorActionListener(this::onSearchKeyboardButtonClick);
    }

    private void OnFavoriteClick(Shop shop) {
        if (isLoggedIn()) {
            if (shop.is_favourite()) mViewModel.requestRemoveFavoriteShop(shop.getId());
            else mViewModel.requestAddFavoriteShop(shop.getId());
        }
    }

    private void onSearchClick(View view) {
        search();
    }

    private boolean onSearchKeyboardButtonClick(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    private void search() {
        mBinding.notFount.setVisibility(GONE);
        keyWard = mBinding.etSearch.getText().toString();
        if (!keyWard.trim().equals("") || keyWard != null) {
            requestSearchShops();
        }
    }

    private void requestSearchShops() {
        mViewModel.requestSearchShops(SearchShopsRequest.builder()
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .keyword(keyWard)
                .build());

    }

    private void onShopsResponse(ShopsResponse response) {
        if (!response.getData().isEmpty()) {
             Timber.d("is not empty");
            mBinding.shops.setVisibility(VISIBLE);
            shopsAdapter.setData(response.getData());
        }
        else {
            Timber.d("is empty");
            mBinding.shops.setVisibility(GONE);
            mBinding.notFount.setVisibility(VISIBLE);
        }


    }

    private void onShopClick(Shop shop) {
        startActivity(new Intent(this, ShopDetailsActivity.class).putExtra(OBJECT, shop));
    }


}