package com.linkpcom.mitrafast.Classes.Dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.linkpcom.mitrafast.databinding.DialogTermsBinding;


public class TermsDialog {


    DialogTermsBinding mBinding;
    private BottomSheetDialog mDialog;

    public TermsDialog(Context context) {
        mDialog = new BottomSheetDialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mBinding = DialogTermsBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        mBinding.agree.setOnClickListener(this::onAgreeClick);

    }


    private void onAgreeClick(View view) {
        mDialog.dismiss();
    }


    public void setContent(String title) {
        mBinding.terms.setText(title);
    }

    public void show() {
        mDialog.show();
    }
}
