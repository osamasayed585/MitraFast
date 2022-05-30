package com.linkpcom.mitrafast.MVVM.Agent.FinancialMovements;


import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.linkpcom.mitrafast.Classes.Adapters.FinancialMovementsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FinancialMovementsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityFinancialMovementsBinding;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class FinancialMovementsActivity extends BaseActivity {
    ActivityFinancialMovementsBinding mBinding;

    FinancialMovementsViewModel mViewModel;

    FinancialMovementsAdapter financialMovementsAdapter;

    FinancialMovementsResponse financialMovementsResponse;
    private double finalCost = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFinancialMovementsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(FinancialMovementsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getFinancialMovementsResponseMutableLiveData().observe(this, this::onFinancialMovementsResponse);
        mViewModel.getPaymentResponseMutableLiveData().observe(this, this::onPaymentResponse);

        //New Instances
        financialMovementsAdapter = new FinancialMovementsAdapter();

        mBinding.financialMovements.setAdapter(financialMovementsAdapter);

        mBinding.commissionPay.setOnClickListener(this::onCommissionPayClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestWalletBalance();
    }

    private void onCommissionPayClick(View view) {

        if (finalCost > 1) {

            mViewModel.requestPayment(PaymentRequest.builder()
                    .payment_type("creditcard")
                    .redirect_url("app://mitrafast/financialMovement")
                    .price(finalCost)
                    .build());
        } else {
            Snackbar.make(view, getResources().getString(R.string.validation_failed), BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    private void onPaymentResponse(PaymentResponse response) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(response.getData().getUrl()));
        startActivity(intent);
    }

    private void onFinancialMovementsResponse(WalletResponse response) {
        Log.d("osama_osama", "onFinancialMovementsResponse: "+Double.parseDouble(response.getData().getValue()));
        if (response.getData().getValue().contains("-")) {
            finalCost = Math.abs(Double.parseDouble(response.getData().getValue()));
            mBinding.totalCommission.setText(String.format("%s %s", finalCost, response.getData().getCurrency()));

        } else {
            mBinding.commissionPay.setVisibility(View.GONE);
            mBinding.totalCommission.setText(getResources().getString(R.string.not));
        }

    }


}