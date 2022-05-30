package com.linkpcom.mitrafast.Classes.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;


import com.linkpcom.mitrafast.databinding.DialogConfirmBinding;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;


public class ConfirmDialog {


    private DialogConfirmBinding mBinding;
    private Dialog mDialog;
    private ConfirmListDialogHandler mClickHandler;

    @Inject
    public ConfirmDialog(@ActivityContext Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mBinding = DialogConfirmBinding.inflate(LayoutInflater.from(context));
        mDialog.setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        mBinding.dismissButton.setOnClickListener(this::onDismissClick);
        mBinding.confirm.setOnClickListener(this::onConfirmClick);
    }

    private void onConfirmClick(View view) {
        mDialog.dismiss();
        if (mClickHandler != null)
            mClickHandler.onConfirmClick();
    }

    private void onDismissClick(View view) {
        mDialog.dismiss();
    }

    public void setTitle(String title) {
        mBinding.title.setText(title);
    }

    public interface ConfirmListDialogHandler {
        void onConfirmClick();
    }

    public void setConfirmListDialogHandler(ConfirmListDialogHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
