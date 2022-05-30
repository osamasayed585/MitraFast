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



import com.linkpcom.mitrafast.Classes.Adapters.OptionsDialogAdapter;
import com.linkpcom.mitrafast.databinding.DialogListBinding;

import java.util.List;


public class ListDialog {


    DialogListBinding mBinding;
    OptionsDialogAdapter optionsDialogAdapter;
    private Dialog mDialog;
//    int selectedItemPosition;

    public ListDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);

        mBinding = DialogListBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        optionsDialogAdapter = new OptionsDialogAdapter();
        mBinding.options.setAdapter(optionsDialogAdapter);
    }

    public void setDismissClickListener(View.OnClickListener listener) {
        mBinding.dismiss.setOnClickListener(listener);
    }
    public void otherButton(View.OnClickListener onClickListener){
        mBinding.tvOther.setVisibility(View.VISIBLE);
        mBinding.tvOther.setOnClickListener(onClickListener);
    }

    public void setData(List<String> options) {
        optionsDialogAdapter.setData(options);
    }

    public void setOnItemClickListener(OptionsDialogAdapter.OptionsDialogAdapterOnClickHandler clickHandler) {
        optionsDialogAdapter.setItemClickListener(clickHandler);
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
