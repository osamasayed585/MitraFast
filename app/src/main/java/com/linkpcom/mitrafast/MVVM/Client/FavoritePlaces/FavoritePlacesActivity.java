package com.linkpcom.mitrafast.MVVM.Client.FavoritePlaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.Adapters.FavoritePlacesAdapter;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.UI.EndlessRecyclerViewScrollListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritePlace;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FavoritePlacesResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.PickLocationMap.PickLocationMapActivity;
import com.linkpcom.mitrafast.databinding.ActivityFavoritePlacesBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.REQUEST_CODE;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavoritePlacesActivity extends BaseActivity implements FavoritePlacesAdapter.FavoritePlacesAdapterOnClickHandler {

    private ActivityFavoritePlacesBinding mBinding;
    private FavoritePlacesViewModel mViewModel;
    private FavoritePlacesAdapter myAddressesAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFavoritePlacesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(FavoritePlacesViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        //Observer
        mViewModel.getFavoritePlacesResponseMutableLiveData().observe(this, this::onFavoritePlacesResponse);

        //New Instances
        myAddressesAdapter = new FavoritePlacesAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mViewModel.requestFavoritePlaces(page);
            }
        };
        myAddressesAdapter.setItemClickListener(this);
        mBinding.favoritePlacesRecyclerView.setAdapter(myAddressesAdapter);
        mBinding.favoritePlacesRecyclerView.setLayoutManager(linearLayoutManager);
        mBinding.favoritePlacesRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            myAddressesAdapter.clear();
            endlessRecyclerViewScrollListener.resetState();
            mViewModel.requestFavoritePlaces(1);
            mBinding.swipeRefreshLayout.setRefreshing(false);
        });

        mBinding.addLocationButton.setOnClickListener(this::onAddLocationClick);
    }

    private void onAddLocationClick(View view) {
        Intent intent = new Intent(this, PickLocationMapActivity.class);
        startActivity(intent);
    }

    private void onFavoritePlacesResponse(FavoritePlacesResponse response) {
        myAddressesAdapter.setData(response.getData());
        mBinding.noAddressesHolder.setVisibility(myAddressesAdapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
        mBinding.tvLabel.setVisibility(getIntent().hasExtra(REQUEST_CODE) && !myAddressesAdapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAddressesAdapter.clear();
        endlessRecyclerViewScrollListener.resetState();
        mViewModel.requestFavoritePlaces(1);
    }

    @Override
    public void onFavoritePlaceItemClick(FavoritePlace favoritePlace) {
        if (getIntent().hasExtra(CONSTANTS.INTENTS.REQUEST_CODE)) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(CONSTANTS.INTENTS.OBJECT, favoritePlace);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public void onFavoritePlaceDeleteClick(int addressId) {
        mBinding.noAddressesHolder.setVisibility(myAddressesAdapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
        mViewModel.requestRemoveFavoritePlace(addressId);
    }


}