package com.linkpcom.mitrafast.MVVM.Client.Offers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.OffersAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Offer;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OffersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.AllShops.AllShopsActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityOffersBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class OffersActivity extends BaseActivity {
    ActivityOffersBinding mBinding;

    OffersViewModel mViewModel;

    private OffersAdapter offersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOffersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(OffersViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        //Request And Observer
        mViewModel.requestOffers().observe(this, this::onOffersResponse);

        offersAdapter = new OffersAdapter();

        mBinding.rvOffers.setAdapter(offersAdapter);
        offersAdapter.setItemClickListener(this::onOfferClick);

    }

    private void onOffersResponse(OffersResponse response) {
        offersAdapter.setData(response.getData());

    }

    private void onOfferClick(Offer offer) {
        switch (offer.getUrl_type_id()) {
            case 1:
                startActivity(new Intent(this, AllShopsActivity.class));
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(offer.getOffer_url()));
                startActivity(intent);
                break;
            case 3:
                startActivity(new Intent(this, ShopDetailsActivity.class).putExtra(ID, offer.getShop_id()));
                break;
        }
    }


}