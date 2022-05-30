package com.linkpcom.mitrafast.MVVM.Client.AddPaymentCard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentCardRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAddPaymentCardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class AddPaymentCardActivity extends BaseActivity {
    ActivityAddPaymentCardBinding mBinding;
    private AddPaymentCardViewModel mViewModel;


    MessageDialog messageDialog;
    private int card_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddPaymentCardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AddPaymentCardViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.cardValidation.observe(this, this::onValidResponse);
        mViewModel.cardIsValid.observe(this, this::onCardValidationResponse);
        mViewModel.getAddPaymentCardResponseMutableLiveData().observe(this, this::onAddPaymentCardResponse);

        //New Instances
        messageDialog = new MessageDialog(this);

        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);

        // add color for first word to hint
        customColor(getResources().getString(R.string.note), getResources().getString(R.string.note_1_will_be_withdrawn_to_validate_the_card_is_then_returned_to_your_wallet_balance_automatically));

    }

    private void onCardValidationResponse(BaseResponse baseResponse) {
        finish();
    }

    private void onValidResponse(PaymentResponse response) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(response.getData().getUrl()));
        startActivity(intent);
        finish();

    }


    private void onSendButtonClicked(View view) {
        if (!validation())
            return;

        if (!validDate())
            return;

        mViewModel.requestAddPaymentCard(getData());
    }


    private void onAddPaymentCardResponse(PaymentCardResponse response) {
        if (response.isSuccess()) {
            card_id = response.getData().getId();
            preferencesManager.setCardId(card_id);
            mViewModel.requestValidateCard(AddPaymentRequest.builder()
                    .creditcard_id(String.valueOf(card_id))
                    .payment_type("creditcard")
                    .redirect_url("app://mitrafast/checkCredit")
                    .build());

        }
    }


    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.etCardNumber) &&
                        validator.isGreaterThan(mBinding.etCardNumber) &&
                        validator.isNotEmpty(mBinding.etName) &&
                        validator.isNotEmpty(mBinding.etYear) &&
                        validator.isYearLengthValid(mBinding.etYear) &&
                        validator.isNotEmpty(mBinding.etMonth) &&
                        validator.isMonthLengthValid(mBinding.etMonth);


    }

    private boolean validDate() {

        String month = mBinding.etMonth.getText().toString();
        String year = mBinding.etYear.getText().toString();

        if (validator.isDateValid(month, year)) {
            return true;
        } else {
            Snackbar.make(mBinding.etMonth, getResources().getString(R.string.errorDate), BaseTransientBottomBar.LENGTH_LONG).show();
            return false;
        }
    }

    private AddPaymentCardRequest getData() {
        return AddPaymentCardRequest.builder()
                .credit_card_number(mBinding.etCardNumber.getText().toString())
                .credit_card_name(mBinding.etName.getText().toString())
                .credit_card_year(mBinding.etYear.getText().toString())
                .credit_card_month(mBinding.etMonth.getText().toString())
                .build();
    }

    private void customColor(String firstWord, String secondWord) {
        SpannableString spannableString = new SpannableString(firstWord + secondWord);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, firstWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), firstWord.length(), secondWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


}
