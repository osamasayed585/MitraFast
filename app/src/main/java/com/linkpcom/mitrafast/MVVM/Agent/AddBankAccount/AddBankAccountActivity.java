package com.linkpcom.mitrafast.MVVM.Agent.AddBankAccount;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Bank;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BanksResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityAddBankAccountBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddBankAccountActivity extends BaseActivity {

    ActivityAddBankAccountBinding mBinding;
    AddBankAccountViewModel mViewModel;


    ListDialog banksDialog;
    List<Bank> banks;
    Bank selectedBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddBankAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AddBankAccountViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getBanksResponseMutableLiveData().observe(this, this::onBanksResponse);
        mViewModel.getAddBnkAccountResponseMutableLiveData().observe(this, this::onAddBankAccountResponse);

        //New Instances
        banksDialog = new ListDialog(this);


        mBinding.bank.setOnClickListener(this::onBankClick);

        mBinding.btnAdd.setOnClickListener(this::onAddBankAccountClick);
        mViewModel.requestBanks();


    }

    private void onAddBankAccountResponse(BaseResponse response) {
        Log.d("OSama_OSama", "onAddBankAccountResponse: finish");
        finish();
    }

    private void onBanksResponse(BanksResponse response) {
        banks = response.getData();
        banksDialog.setData(response.getBanksNames());

        banksDialog.setOnItemClickListener(clickedItemPosition -> {
            selectedBank = banks.get(clickedItemPosition);
            mBinding.bank.setText(selectedBank.getName());
            banksDialog.getDialog().dismiss();
        });
        banksDialog.setDismissClickListener(view -> banksDialog.getDialog().dismiss());
    }

    private void onBankClick(View view) {
        banksDialog.getDialog().show();
    }

    private void onAddBankAccountClick(View view) {
        if (!validation())
            return;
        mViewModel.requestAddBankAccount(getData());

    }

    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.etAccountOwnerName) &&
                        validator.isNotEmpty(mBinding.etIBANNumber) &&
                        validator.isIban(mBinding.etIBANNumber.getText().toString(), mBinding.etIBANNumber, 22) &&
                        validator.isNotNull(selectedBank, mBinding.bank);
    }

    private AddBankAccountRequest getData() {
        return AddBankAccountRequest.builder()
                .account_name(mBinding.etAccountOwnerName.getText().toString())
                .iban_number("SA" + mBinding.etIBANNumber.getText().toString())
                .bank_id(selectedBank.getId())
                .build();
    }


}