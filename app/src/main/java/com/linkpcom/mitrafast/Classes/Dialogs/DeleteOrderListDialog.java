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



import com.linkpcom.mitrafast.Classes.Adapters.DeleteOrderDialogAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CancelReason;
import com.linkpcom.mitrafast.databinding.DialogDeleteOrderBinding;

import java.util.List;


public class DeleteOrderListDialog {


    DialogDeleteOrderBinding mBinding;
    DeleteOrderDialogAdapter deleteOrderDialogAdapter;
    private Dialog mDialog;

    public DeleteOrderListDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);

        mBinding = DialogDeleteOrderBinding.inflate(LayoutInflater.from(context));

        mDialog.setContentView(mBinding.getRoot());

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 32);

        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        deleteOrderDialogAdapter = new DeleteOrderDialogAdapter();
        mBinding.options.setAdapter(deleteOrderDialogAdapter);

        mBinding.dismissButton.setOnClickListener(this::onDismissClick);
        mBinding.confirm.setOnClickListener(this::onConfirmClick);

    }

    private void onConfirmClick(View view) {
        mDialog.dismiss();
        if (mClickHandler != null)
            mClickHandler.onConfirmClick(deleteOrderDialogAdapter.getData().get(deleteOrderDialogAdapter.getSelectedItemPosition()));
    }

    private void onDismissClick(View view) {
        mDialog.dismiss();
    }


    public interface DeleteOrderListDialogHandler {
        void onConfirmClick(CancelReason selectedCancelOrder);
    }

    private DeleteOrderListDialogHandler mClickHandler;


    public void setDeleteOrderListDialogHandler(DeleteOrderListDialogHandler clickHandler) {
        mClickHandler = clickHandler;
    }



    public void setData(List<CancelReason> cancelReasons) {
        deleteOrderDialogAdapter.setData(cancelReasons);
    }



    public Dialog getDialog() {
        return mDialog;
    }
}
