package com.linkpcom.mitrafast.MVVM.Client.Support3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.SupportAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Support;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SupportResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.SupportQuestions.SupportQuestionsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Tickets.TicketsActivity;
import com.linkpcom.mitrafast.databinding.ActivitySupport3Binding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Support3Activity extends BaseActivity {
    ActivitySupport3Binding mBinding;
    Support3ViewModel mViewModel;

    SupportAdapter supportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySupport3Binding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(Support3ViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getSupportResponseMutableLiveData().observe(this, this::onSupportResponse);

        //New Instance
        supportAdapter = new SupportAdapter();

        supportAdapter.setItemClickListener(this::onSupportItemClick);
        mBinding.rvSupport.setAdapter(supportAdapter);

        mBinding.tvMyRequests.setOnClickListener(this::onMyRequestsClick);
        mViewModel.requestSupport();
    }

    private void onMyRequestsClick(View view) {
        if (!isLoggedIn())
            return;

        startActivity(new Intent(this, TicketsActivity.class));
    }

    private void onSupportItemClick(Support support) {
        Intent intent = new Intent(this, SupportQuestionsActivity.class);
        intent.putExtra(OBJECT, support);
        startActivity(intent);
    }


    private void onSupportResponse(SupportResponse response) {
        supportAdapter.setData(response.getData());
    }


}