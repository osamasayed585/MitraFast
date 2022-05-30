package com.linkpcom.mitrafast.MVVM.Client.RateAgent;


import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateAgentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.databinding.ActivityRateAgentBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RateAgentActivity extends BaseActivity {

    public static final int DEFAULT_VALUE = -1;
    ActivityRateAgentBinding mBinding;
    RateAgentViewModel mViewModel;


    int orderId;
    String orderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRateAgentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getIntExtra(CONSTANTS.INTENTS.ID, DEFAULT_VALUE);
        orderDate = getIntent().getStringExtra(CONSTANTS.INTENTS.DATE);

        mBinding.orderId.setText(String.format("#%s", orderId));
        mBinding.orderDate.setText(orderDate);


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(RateAgentViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getRateAgentResponseMutableLiveData().observe(this, this::onRateAgentResponse);


        mBinding.rateAgent.setOnClickListener(this::onRateAgentClick);
        mBinding.home.setOnClickListener(this::onHomeClick);

    }

    @Override
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void onRateAgentClick(View view) {
        mViewModel.requestRateAgent(getData());
    }

    private RateAgentRequest getData() {
        return RateAgentRequest.builder()
                .order_id(orderId)
                .client_rate(String.valueOf(mBinding.rating.getRating()))
                .build();
    }


    private void onRateAgentResponse(BaseResponse response) {
        finish();
    }
}