package com.linkpcom.mitrafast.MVVM.Common.ChangeForgottenPassword;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityChangeForgottenPasswordBinding;


import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ChangeForgottenPasswordActivity extends BaseActivity {

    ActivityChangeForgottenPasswordBinding mBinding;
    ChangeForgottenPasswordViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityChangeForgottenPasswordBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ChangeForgottenPasswordViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);


        mViewModel.getChangeForgottenPasswordResponseMutableLiveData().observe(this, this::onChangeForgottenPasswordResponse);


        mBinding.confirm.setOnClickListener(this::onConfirmClick);


    }


    private void onConfirmClick(View view) {
        if (!validation())
            return;
        mViewModel.requestChangeForgottenPassword(getData());

    }


    private void onChangeForgottenPasswordResponse(BaseResponse response) {
        preferencesManager.setResetPassword(false);
        setResult(Activity.RESULT_OK);
        finish();

    }

    private boolean validation() {



        return
                validator.isNotEmpty(mBinding.etPassword) &&
                        validator.isNotEmpty(mBinding.etConfirmPassword) &&
                        validator.passwordsMatches(mBinding.etPassword.getText().toString(), mBinding.etConfirmPassword.getText().toString());

    }

    private String getData() {
        return mBinding.etPassword.getText().toString();
    }




}