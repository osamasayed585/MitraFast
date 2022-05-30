package com.linkpcom.mitrafast.MVVM.Client.AllShops;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.ShopsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AllShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityAllShopsBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class AllShopsActivity extends BaseActivity {
    private ActivityAllShopsBinding mBinding;
    AllShopsViewModel mViewModel;
    ShopsAdapter shopsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAllShopsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AllShopsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getShopsResponseMutableLiveData().observe(this, this::onShopsResponse);

        //New Instances
        shopsAdapter = new ShopsAdapter();

        shopsAdapter.setPreferencesManager(preferencesManager);

        shopsAdapter.setItemClickListener(this::onShopClick);


        mBinding.shops.setAdapter(shopsAdapter);

        requestAllShops();

    }


    private void requestAllShops() {
        mViewModel.requestAllShops(AllShopsRequest.builder()
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .build());

    }

    private void onShopsResponse(ShopsResponse response) {
        shopsAdapter.setData(response.getData());
    }

    private void onShopClick(Shop shop) {
        startActivity(new Intent(this, ShopDetailsActivity.class).putExtra(OBJECT, shop));
    }


}