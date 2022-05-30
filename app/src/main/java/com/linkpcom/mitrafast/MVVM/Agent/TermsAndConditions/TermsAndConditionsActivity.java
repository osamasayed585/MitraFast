package com.linkpcom.mitrafast.MVVM.Agent.TermsAndConditions;


import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityTermsAndConditionsBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class TermsAndConditionsActivity extends BaseActivity {
    ActivityTermsAndConditionsBinding mBinding;
    private TermsAndConditionsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTermsAndConditionsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(TermsAndConditionsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getTermsAndConditionsResponseMutableLiveData().observe(this, this::onTermsAndConditionsResponse);

        mViewModel.requestTermsAndConditions(3);
    }


    private void onTermsAndConditionsResponse(TermsAndConditionsResponse response) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.content.setText(Html.fromHtml(response.getData().getTerms(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            mBinding.content.setText(Html.fromHtml(response.getData().getTerms()));
        }
    }

}