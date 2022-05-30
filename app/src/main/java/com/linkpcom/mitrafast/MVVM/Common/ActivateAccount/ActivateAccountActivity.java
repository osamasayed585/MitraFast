package com.linkpcom.mitrafast.MVVM.Common.ActivateAccount;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ClientProfileResponse;
import com.linkpcom.mitrafast.Classes.Utils.FirebasePhoneAuth;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityActivateAccountBinding;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ActivateAccountActivity extends BaseActivity implements FirebasePhoneAuth.PhoneAuthHandler {
    ActivityActivateAccountBinding mBinding;
    ActivateAccountViewModel mViewModel;


    String mobilePhoneNumber;

    FirebasePhoneAuth firebasePhoneAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityActivateAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mobilePhoneNumber = getIntent().getStringExtra(CONSTANTS.INTENTS.MOBILE_PHONE_NUMBER);

        String language = getResources().getConfiguration().locale.getLanguage();
        String numForArabic;
        if (language.equals("ar")) {
            numForArabic = mobilePhoneNumber.substring(1);
        } else numForArabic = mobilePhoneNumber;

        mBinding.tvVerifyHasBeenSent.setText(
                String.format("%s %s+\n%s",
                        getResources().getString(R.string.activation_code_has_been_sent_1),
                        numForArabic,
                        getResources().getString(R.string.activation_code_has_been_sent_2)
                ));

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ActivateAccountViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);


        //Request And Observer
        mViewModel.getActivateAccountResponseMutableLiveData().observe(this, this::onActivateAccountResponse);

        set1MinTimer();

        mBinding.active.setOnClickListener(this::onActiveButtonClick);
        mBinding.resendCode.setOnClickListener(this::onResendCodeClick);
        mBinding.tvEditNumber.setOnClickListener(this::onEditNumberClick);

        //New Instances
        firebasePhoneAuth = new FirebasePhoneAuth(this);

        firebasePhoneAuth.setPhoneAuthHandler(this);

        firebasePhoneAuth.onCreate(savedInstanceState);

        firebasePhoneAuth.startPhoneNumberVerification(mobilePhoneNumber);

    }

    private void onEditNumberClick(View view) {
        finish();
    }


    private void onActivateAccountResponse(ClientProfileResponse response) {
        if (response.getData().isUserVerify())
            preferencesManager.setUserVerified(true);
        if (response.getData().is_data_complete())
            preferencesManager.setProfileComplete(true);
        setResult(Activity.RESULT_OK);
        finish();

    }


    //Common Between Activities
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //To Clear Access Token If He Didn't Complete Activation Process
        if (!preferencesManager.isUserVerified())
            preferencesManager.removeUser();
    }


    private void set1MinTimer() {
        mBinding.resendCode.setClickable(false);
        mBinding.resendCode.setEnabled(false);
        mBinding.resendCode.setTextColor(getResources().getColor(R.color.black));

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                mBinding.resendCode.setText(String.format("%s %s:%s %s",
                        getString(R.string.remaining),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),
                        getString(R.string.for_resend)));
            }

            public void onFinish() {
                mBinding.resendCode.setText(getString(R.string.resend_code));
                mBinding.resendCode.setTextColor(getResources().getColor(R.color.red));
                mBinding.resendCode.setClickable(true);
                mBinding.resendCode.setEnabled(true);
            }

        }.start();
    }

    private void onResendCodeClick(View view) {
        firebasePhoneAuth.resendVerificationCode(mobilePhoneNumber);
        set1MinTimer();
    }


    private void onActiveButtonClick(View view) {
        if (!validation())
            return;
        String text = mBinding.tvEditCode.getText();
        firebasePhoneAuth.verifyPhoneNumberWithCode(text);
        Timber.d("edit text is : %s", text);
    }

    private boolean validation() {

        return
                validator.isNotEmpty(mBinding.tvEditCode.getText(), getString(R.string.errorRequired));
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }


    @Override
    public void onPhoneVerified() {
        mViewModel.requestActivateAccount();
    }
}