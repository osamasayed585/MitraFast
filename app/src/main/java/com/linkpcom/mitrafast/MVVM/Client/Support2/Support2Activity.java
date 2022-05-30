package com.linkpcom.mitrafast.MVVM.Client.Support2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.TermsDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ContactUs.ContactUsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Support3.Support3Activity;
import com.linkpcom.mitrafast.MVVM.Common.FAQ.FAQActivity;
import com.linkpcom.mitrafast.databinding.ActivitySupport2Binding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Support2Activity extends BaseActivity {
    ActivitySupport2Binding mBinding;
    Support2ViewModel mViewModel;
    TermsDialog termsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySupport2Binding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(Support2ViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getTermsResponseMutableLiveData().observe(this, this::onTermsResponse);


        termsDialog = new TermsDialog(this);

        mBinding.tvHelpAndSupport.setOnClickListener(this::onHelpAndSupportClick);
        mBinding.tvFaqs.setOnClickListener(this::onFaqsClick);
        mBinding.tvTerms.setOnClickListener(this::onTermsClick);
        mBinding.tvContactUs.setOnClickListener(this::onContactUsClick);

    }

    private void onContactUsClick(View view) {
        startActivity(new Intent(this, ContactUsActivity.class));
    }

    private void onTermsResponse(TermsAndConditionsResponse response) {
        termsDialog.setContent(response.getData().getTerms());
        termsDialog.show();
    }

    private void onTermsClick(View view) {
        mViewModel.requestTerms(2);
    }

    private void onFaqsClick(View view) {
        startActivity(new Intent(this, FAQActivity.class));
    }

    private void onHelpAndSupportClick(View view) {
        startActivity(new Intent(this, Support3Activity.class));

    }
}