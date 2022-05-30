package com.linkpcom.mitrafast.MVVM.Agent.PaymentStatus;

import android.content.Intent;
import android.os.Bundle;

import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;

import java.util.Objects;

import timber.log.Timber;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class PaymentStatusActivity extends BaseActivity {

    MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);

        //New Instances
        messageDialog = new MessageDialog(this);
        messageDialog.setMessageDialogHandler(this::finish);
        messageDialog.setMainBtnText(getString(R.string.continue_process));

        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("onNewIntent");
        if (Objects.equals(intent.getScheme(), "app")) {
            try {
                String success = intent.getData().getQueryParameter("success");
                if (success.equals("1")) {
                    messageDialog.setTitle(getString(R.string.payment_succeeded));
                } else {
                    messageDialog.setTitle(getString(R.string.payment_failed));
                }
                messageDialog.getDialog().show();

            } catch (Exception e) {
                Timber.e(e, "onNewIntent: ");
            }
        }
    }


}