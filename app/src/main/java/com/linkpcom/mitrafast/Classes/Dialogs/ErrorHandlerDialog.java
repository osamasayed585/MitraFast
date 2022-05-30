package com.linkpcom.mitrafast.Classes.Dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.Common.Login.LoginActivity;
import com.linkpcom.mitrafast.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;
import io.reactivex.subjects.PublishSubject;


public class ErrorHandlerDialog {

    private Context context;
    private AlertDialog errorAlertDialog;
    private stopService mStopService;
    public PublishSubject<Boolean> mStopMail;
    @Inject
    PreferencesManager mPreferencesManager;

    @Inject
    public ErrorHandlerDialog(@ActivityContext Context context, AlertDialog errorAlertDialog) {
        this.context = context;
        this.errorAlertDialog = errorAlertDialog;

        errorAlertDialog.setTitle(context.getString(R.string.error));
        errorAlertDialog.setCancelable(false);
    }

    public void setBaseResponse(BaseResponse response, DialogInterface.OnClickListener onClickListener) {
        if (response.getStatusCode() == 503) {
            errorAlertDialog.setMessage(context.getString(R.string.please_check_your_internet_connection));
            errorAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.ok), onClickListener);
        } else if (response.getStatusCode() == 401) {
            errorAlertDialog.setMessage(response.getMessage());
            errorAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.ok), (dialog, which) -> {

                mStopMail = PublishSubject.create();

                mStopMail.onNext(false);

                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            });
        } else {
            errorAlertDialog.setMessage(response.getMessage());
            errorAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.ok), (dialog, which) -> errorAlertDialog.dismiss());
        }

    }

    public void show() {
        errorAlertDialog.show();
    }

    public void dismiss() {
        errorAlertDialog.dismiss();
    }

    public interface stopService {
        void onStopService();
    }
}
