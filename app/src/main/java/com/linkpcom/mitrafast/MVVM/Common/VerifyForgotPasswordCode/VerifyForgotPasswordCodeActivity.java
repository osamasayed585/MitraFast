package com.linkpcom.mitrafast.MVVM.Common.VerifyForgotPasswordCode;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.CHANGE_FORGOTTEN_PASSWORD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.annotation.Nullable;

import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.Utils.FirebasePhoneAuth;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Common.ChangeForgottenPassword.ChangeForgottenPasswordActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityVerifyForgotPasswordCodeBinding;

import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VerifyForgotPasswordCodeActivity extends BaseActivity implements FirebasePhoneAuth.PhoneAuthHandler {
    ActivityVerifyForgotPasswordCodeBinding mBinding;


    String mobilePhoneNumber;

    FirebasePhoneAuth firebasePhoneAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVerifyForgotPasswordCodeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mobilePhoneNumber = getIntent().getStringExtra(CONSTANTS.INTENTS.MOBILE_PHONE_NUMBER);


        set1MinTimer();

        mBinding.active.setOnClickListener(this::onActiveButtonClick);
        mBinding.resendCode.setOnClickListener(this::onResendCodeClick);


        //New Instances
        firebasePhoneAuth = new FirebasePhoneAuth(this);

        firebasePhoneAuth.setPhoneAuthHandler(this);

        firebasePhoneAuth.onCreate(savedInstanceState);

        firebasePhoneAuth.startPhoneNumberVerification(mobilePhoneNumber);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_FORGOTTEN_PASSWORD)
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
    }

    //Common Between Activities
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //To Clear Access Token If He Didn't Reset Password
        if (preferencesManager.isResetPassword())
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
        firebasePhoneAuth.verifyPhoneNumberWithCode(getData());

    }

    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.tvEditCode.getText(), getString(R.string.errorRequired));
    }

    private String getData() {
        return mBinding.tvEditCode.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onPhoneVerified() {
        startActivityForResult(new Intent(VerifyForgotPasswordCodeActivity.this, ChangeForgottenPasswordActivity.class), CHANGE_FORGOTTEN_PASSWORD);
    }

}