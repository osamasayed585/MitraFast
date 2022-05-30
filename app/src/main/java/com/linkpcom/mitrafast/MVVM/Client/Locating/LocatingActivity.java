package com.linkpcom.mitrafast.MVVM.Client.Locating;

import android.content.Intent;
import android.os.Bundle;


import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.DeleteOrderListDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.AgentNode;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CancelOrderReasonsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderSuccessfullyListed.OrderSuccessfullyListedActivity;
import com.linkpcom.mitrafast.databinding.ActivityLocatingBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LocatingActivity extends BaseActivity {

    ActivityLocatingBinding mBinding;
    LocatingViewModel mViewModel;


    DeleteOrderListDialog deleteOrderListDialog;


    String orderId;


    Order mOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLocatingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mOrder = getIntent().getParcelableExtra(OBJECT);

        orderId = String.valueOf(mOrder.getId());


        //New Instances
        deleteOrderListDialog = new DeleteOrderListDialog(this);


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(LocatingViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getCancelOrderReasonsResponseMutableLiveData().observe(this, this::onCancelOrderReasonsResponse);
        mViewModel.getCancelOrderResponseMutableLiveData().observe(this, this::onCancelOrderResponse);

        mViewModel.getAgentsMutableLiveData().observe(this, this::onAgentsResponse);


        mViewModel.startAgentsListening(orderId);

        mBinding.cancelOrder.setOnClickListener(view -> mViewModel.requestCancelOrderReasons());

    }


    private void onCancelOrderReasonsResponse(CancelOrderReasonsResponse response) {
        deleteOrderListDialog.setData(response.getData());
        deleteOrderListDialog.setDeleteOrderListDialogHandler(selectedCancelOrder -> {
            mViewModel.requestUserCancelOrder(CancelOrderRequest.builder()
                    .order_id(orderId)
                    .cancellation_reason_id(String.valueOf(selectedCancelOrder.getId()))
                    .build());
        });
        deleteOrderListDialog.getDialog().show();

    }


    private void onCancelOrderResponse(BaseResponse response) {
        deleteOrderListDialog.getDialog().dismiss();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    private void onAgentsResponse(AgentNode agentNode) {
        finish();
        startActivity(new Intent(this, OrderSuccessfullyListedActivity.class).putExtra(ID, orderId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.removeAllListeners();
    }

    @Override
    public void onBackPressed() {
        mViewModel.requestCancelOrderReasons();
    }


}