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

import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogTicketBinding;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

public class ClientTicketDialog extends Dialog {

    private final DialogTicketBinding mBinding;
    private final Context mContext;


    @Inject
    public ClientTicketDialog(@ActivityContext Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        mBinding = DialogTicketBinding.inflate(LayoutInflater.from(context), null, false);
        setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

//        getDialog().getWindow().setBackgroundDrawable(inset);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        mBinding.btnCancel.setOnClickListener(this::onDismissClick);
    }

    public void setData(int order_id, String name, String address, String ohine) {
        mBinding.tvName.setText(name);
//        mBinding.tvAddress.setText(address);
        mBinding.tvPhone.setText(ohine);
        mBinding.tvOrderNumber.setText(String.format("%s", order_id));
    }

    public void setOnConfirmClicked(View.OnClickListener view) {
        dismiss();
        mBinding.btnReport.setOnClickListener(view);
    }

    private void onDismissClick(View view) {
        dismiss();
    }

    public String getMessage() {
        if(mBinding.etMessage.getText().toString().equals("")) {
            mBinding.etMessage.setError(mContext.getString(R.string.errorRequired));
            return null;
        }
        return mBinding.etMessage.getText().toString();
    }
}
