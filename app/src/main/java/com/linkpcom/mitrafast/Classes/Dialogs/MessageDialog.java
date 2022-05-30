package com.linkpcom.mitrafast.Classes.Dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;



import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogMessageBinding;

public class MessageDialog {

    DialogMessageBinding mBinding;
    private Dialog mDialog;
    Context context;

    public MessageDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context, android.R.style.Theme_Light);
        mDialog.setCancelable(false);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DialogMessageBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        mDialog.getWindow().setBackgroundDrawableResource(R.color.lightGray);

        mBinding.main.setOnClickListener(this::onBackToMainClick);
    }

    private void onBackToMainClick(View view) {
        mDialog.dismiss();
        if (mClickHandler != null)
            mClickHandler.onBackToMainClick();
    }

    public void setTitle(String title) {
        mBinding.title.setText(title);
    }

    //The interface that will be implemented later in parent activity
    public interface MessageDialogHandler {
        void onBackToMainClick();
    }

    private MessageDialogHandler mClickHandler;


    public void setMessageDialogHandler(MessageDialogHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public void setMainBtnText(String text) {
        mBinding.main.setText(text);
    }

    public Dialog getDialog() {
        return mDialog;
    }


}


