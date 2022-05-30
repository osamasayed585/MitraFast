package com.linkpcom.mitrafast.Classes.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogImageBinding;

import java.io.File;


public class ImageDialog {


    DialogImageBinding mBinding;
    Context context;
    private Dialog mDialog;

    public ImageDialog(Context context) {
        this.context = context;
        mDialog = new Dialog(context, android.R.style.Theme_Light);

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mBinding = DialogImageBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.Animation_Design_BottomSheetDialog;
    }


    public void setImage(String ImagePath) {
        //If Internal Memory Image
        File imgFile = new File(ImagePath);
        if (imgFile.exists())
            Picasso.get()
                    .load(imgFile)
                    .placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.image);

            //If URL Image
        else
            Picasso.get()
                    .load(ImagePath)
                    .placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.image);

    }


    public Dialog getDialog() {
        return mDialog;
    }


}
