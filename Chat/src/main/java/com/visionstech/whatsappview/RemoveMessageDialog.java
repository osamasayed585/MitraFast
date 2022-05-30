package com.visionstech.whatsappview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.visionstech.whatsappview.databinding.DialogRemoveMessageBinding;


public class RemoveMessageDialog {


    DialogRemoveMessageBinding binding;
    private Dialog mDialog;
    private RemoveMessageDialogHandler mRemoveMessageDialogHandler;

    public RemoveMessageDialog(Context context) {
        mDialog = new Dialog(context);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        binding = DialogRemoveMessageBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(binding.getRoot());
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        binding.dismessButton.setOnClickListener(this::onNegativeClick);
        binding.okButton.setOnClickListener(this::onPositiveClick);

    }

    private void onNegativeClick(View view) {
        mDialog.dismiss();
    }

    private void onPositiveClick(View view) {
        mDialog.dismiss();
        if (mRemoveMessageDialogHandler != null)
            mRemoveMessageDialogHandler.onPositiveClick();
    }


    public void setNegativeListener(View.OnClickListener listener) {
        binding.dismessButton.setOnClickListener(listener);
    }


    public void setRemoveMessageDialogHandler(RemoveMessageDialogHandler generalDialogHandler) {
        mRemoveMessageDialogHandler = generalDialogHandler;
    }

    public Dialog getDialog() {
        return mDialog;
    }


    public interface RemoveMessageDialogHandler {
        void onPositiveClick();
    }

}
