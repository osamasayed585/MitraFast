package com.linkpcom.mitrafast.Classes.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogAddCouponBinding;


public class AddCouponDialog {


    DialogAddCouponBinding mBinding;
    private Context context;
    private Dialog mDialog;

    public AddCouponDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mBinding = DialogAddCouponBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 0);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        mBinding.dismissButton.setOnClickListener(this::onDismissClick);
        mBinding.btnAdd.setOnClickListener(this::onAddClick);
    }

    private void onAddClick(View view) {
        if (mBinding.etCode.getText().toString().equals("")) {
            mBinding.etCode.setError(context.getResources().getString(R.string.errorRequired));
            return;
        }

        mDialog.dismiss();
        if (mClickHandler != null)
            mClickHandler.onAddCardClick(mBinding.etCode.getText().toString());
    }

    private void onDismissClick(View view) {
        mDialog.dismiss();
    }


    public void setTitle(String title) {
        mBinding.title.setText(title);
    }

    public interface AddCardListDialogHandler {
        void onAddCardClick(String code);
    }

    private AddCardListDialogHandler mClickHandler;


    public void setAddCardListDialogHandler(AddCardListDialogHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public Dialog getDialog() {
        return mDialog;
    }
}
