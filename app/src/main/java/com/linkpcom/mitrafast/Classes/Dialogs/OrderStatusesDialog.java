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
import android.widget.Toast;



import com.linkpcom.mitrafast.Classes.Adapters.OrderStatusesAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Status;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogOrderStatusesBinding;

import java.util.List;


public class OrderStatusesDialog {


    DialogOrderStatusesBinding mBinding;
    OrderStatusesAdapter orderStatusesAdapter;
    private Dialog mDialog;
    private final Context mContext;
    //    int selectedItemPosition;

    public OrderStatusesDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setCancelable(false);

        mBinding = DialogOrderStatusesBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        orderStatusesAdapter = new OrderStatusesAdapter();
        mBinding.options.setAdapter(orderStatusesAdapter);

        mBinding.dismiss.setOnClickListener(this::onDismissClick);
        mBinding.confirm.setOnClickListener(this::onConfirmClick);

    }

    private void onConfirmClick(View view) {

        if (orderStatusesAdapter.getSelectedItemPosition() == 0) {
            mDialog.dismiss();
            if (mClickHandler != null)
                mClickHandler.onConfirmClick(orderStatusesAdapter.getSelectedItemId());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.you_should_select_statuses_in_order), Toast.LENGTH_SHORT).show();
        }
    }

    private void onDismissClick(View view) {
        mDialog.dismiss();
    }

    public interface OrderStatusesDialogHandler {
        void onConfirmClick(int orderStatusId);
    }

    private OrderStatusesDialogHandler mClickHandler;

    public void setOrderStatusesDialogHandler(OrderStatusesDialogHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    public void setData(List<Status> orderStatuses) {
        orderStatusesAdapter.setData(orderStatuses);
    }

    public Dialog getDialog() {
        return mDialog;
    }


}
