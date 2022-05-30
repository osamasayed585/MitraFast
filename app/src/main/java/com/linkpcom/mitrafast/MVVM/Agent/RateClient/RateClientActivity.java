package com.linkpcom.mitrafast.MVVM.Agent.RateClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateClientRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityRateClientBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class RateClientActivity extends BaseActivity {

    ActivityRateClientBinding mBinding;
    RateClientViewModel mViewModel;


    int orderId;
    String orderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRateClientBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getIntExtra(CONSTANTS.INTENTS.ID, -1);
        orderDate = getIntent().getStringExtra(CONSTANTS.INTENTS.DATE);

        mBinding.orderId.setText(String.format("#%s", orderId));
        mBinding.orderDate.setText(orderDate);

        mBinding.actionBar.tvName.setText(getString(R.string.rate_client));
        mBinding.actionBar.btnBack.setVisibility(View.GONE);
        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(RateClientViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getRateClientResponseMutableLiveData().observe(this, this::onRateClientResponse);


        mBinding.rateClient.setOnClickListener(this::onRateClientClick);
        mBinding.home.setOnClickListener(this::onHomeClick);

    }

    @Override
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private void onRateClientClick(View view) {
        mViewModel.requestRateClient(getData());
    }

    private RateClientRequest getData() {
        return RateClientRequest.builder()
                .order_id(orderId)
                .driver_rate(String.valueOf(mBinding.rating.getRating()))
                .build();
    }


    private void onRateClientResponse(BaseResponse response) {
        finish();
    }
}