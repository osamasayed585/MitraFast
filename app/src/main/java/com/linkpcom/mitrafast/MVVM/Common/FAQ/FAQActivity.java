package com.linkpcom.mitrafast.MVVM.Common.FAQ;

import com.linkpcom.mitrafast.Classes.Adapters.FAQsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FAQResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityFaqBinding;


import android.os.Bundle;


import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class FAQActivity extends BaseActivity {
    ActivityFaqBinding mBinding;
    private FAQViewModel mViewModel;

    FAQsAdapter faqsAdapterAgent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(FAQViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getFAQResponseMutableLiveData().observe(this, this::onFAQResponse);

        //New Instance
        faqsAdapterAgent = new FAQsAdapter();

        mBinding.faqs.setAdapter(faqsAdapterAgent);

        mViewModel.requestFAQ();

    }


    private void onFAQResponse(FAQResponse response) {
        faqsAdapterAgent.setData(response.getData());
    }

}
