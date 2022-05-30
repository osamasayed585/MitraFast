package com.visionstech.whatsappview;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.bumptech.glide.Glide;
import com.visionstech.whatsappview.databinding.DialogChatImageBinding;


public class ChatImagesDialog {

    DialogChatImageBinding binding;
    Context context;
    private Dialog mDialog;

    public ChatImagesDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DialogChatImageBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(binding.getRoot());

        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;

    }

    public void setImage(String ImagePath) {
        Glide.with(context).load(ImagePath).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(binding.image);

    }


    public Dialog getDialog() {
        return mDialog;
    }


}


