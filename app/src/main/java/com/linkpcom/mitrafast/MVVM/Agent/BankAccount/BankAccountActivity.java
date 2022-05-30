package com.linkpcom.mitrafast.MVVM.Agent.BankAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.BankAccountsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BankAccountResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.AddBankAccount.AddBankAccountActivity;
import com.linkpcom.mitrafast.databinding.ActivityBankAccountBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BankAccountActivity extends BaseActivity {
    ActivityBankAccountBinding mBinding;
    private BankAccountViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBankAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(BankAccountViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getBankAccountResponseMutableLiveData().observe(this, this::onBankAccountDetailsResponse);

        mBinding.btnAdd.setOnClickListener(this::onAddBankAccountClick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestBankAccountDetails();
    }

    private void onAddBankAccountClick(View view) {
        startActivity(new Intent(this, AddBankAccountActivity.class));
    }

    private void onBankAccountDetailsResponse(BankAccountResponse response) {

        BankAccountsAdapter bankAccountsAdapter = new BankAccountsAdapter();
        bankAccountsAdapter.setData(response.getData());
        mBinding.bankAccounts.setAdapter(bankAccountsAdapter);
        mBinding.bankAccounts.setHasFixedSize(true);
    }

}