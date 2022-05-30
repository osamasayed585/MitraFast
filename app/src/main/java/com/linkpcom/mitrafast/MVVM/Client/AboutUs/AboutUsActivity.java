package com.linkpcom.mitrafast.MVVM.Client.AboutUs;

import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.AboutUsListsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AboutUsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityAboutUsBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class AboutUsActivity extends BaseActivity {
    ActivityAboutUsBinding mBinding;
    private AboutUsViewModel mViewModel;

    AboutUsListsAdapter aboutUsListsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel =  new ViewModelProvider(this).get(AboutUsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getAboutUsResponseMutableLiveData().observe(this, this::onAboutUsResponse);

        //New Instance
        aboutUsListsAdapter = new AboutUsListsAdapter();

        mBinding.aboutUsList.setAdapter(aboutUsListsAdapter);

        mViewModel.requestAboutUs();

    }

    private void onAboutUsResponse(AboutUsResponse response) {
        aboutUsListsAdapter.setData(response.getData());
    }
}
