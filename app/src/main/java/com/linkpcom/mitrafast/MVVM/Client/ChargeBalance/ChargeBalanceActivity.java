package com.linkpcom.mitrafast.MVVM.Client.ChargeBalance;

import static com.linkpcom.mitrafast.Classes.Others.MainApplication.creditCardId;
import static com.linkpcom.mitrafast.Classes.Others.MainApplication.value;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Wallet.WalletActivity;
import com.linkpcom.mitrafast.R;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ChargeBalanceActivity extends AppCompatActivity {

    private MessageDialog messageDialog;
    private ChargeBalanceViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_balance);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ChargeBalanceViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getChargeResponseMutableLiveData().observe(this, this::onChargeResponse);


        //New Instances
        messageDialog = new MessageDialog(this);
        messageDialog.setMessageDialogHandler(this::onBackToWallet);
        messageDialog.setMainBtnText(getString(R.string.continue_process));

        onNewIntent(getIntent());
    }

    private void onLoading(Boolean aBoolean) {
    }

    private void onApiError(BaseResponse baseResponse) {
    }

    private void onChargeResponse(BaseResponse response) {
        messageDialog.setTitle(getString(R.string.payment_succeeded));
        messageDialog.getDialog().show();
    }

    private void onBackToWallet() {
        startActivity(new Intent(this, WalletActivity.class));
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("Shady Deep Link onNewIntent %s", intent.getData().toString());
        if (intent.getScheme().equals("app")) {
            try {
                String success = intent.getData().getQueryParameter("success");


                if (success.equals("1")) {
                    mViewModel.requestCharge(ChargeRequest.builder()
                            .value(value)
                            .credit_card_id(creditCardId)
                            .build());
                } else {
                    messageDialog.setTitle(getString(R.string.payment_failed));
                    messageDialog.getDialog().show();
                }

            } catch (Exception e) {
                Timber.e(e, "onNewIntent: ");
            }
        }
    }
}