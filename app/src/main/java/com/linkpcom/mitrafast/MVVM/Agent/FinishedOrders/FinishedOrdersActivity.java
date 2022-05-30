package com.linkpcom.mitrafast.MVVM.Agent.FinishedOrders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.OrdersAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder.InProgressOrderActivity;
import com.linkpcom.mitrafast.MVVM.Agent.RateClient.RateClientActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityFinishedOrdersBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.DATE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.RATE_CLIENT;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class FinishedOrdersActivity extends BaseActivity {
    ActivityFinishedOrdersBinding mBinding;

    FinishedOrdersViewModel mViewModel;

    OrdersAdapter finishedOrdersAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFinishedOrdersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(FinishedOrdersViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getFinishedOrdersResponseMutableLiveData().observe(this, this::onFinishedOrdersResponse);

        //New Instances
        finishedOrdersAdapter = new OrdersAdapter();


        mBinding.finishedOrders.setAdapter(finishedOrdersAdapter);

        finishedOrdersAdapter.setItemClickListener(this::onOrderClick);

        mViewModel.requestFinishedOrders();
    }


    private void onOrderClick(Order order) {
        if (order.getOrder_status().getId() == 7 && order.getRate().getDriver_rate() == null) {
            Intent intent = new Intent(this, RateClientActivity.class);
            intent.putExtra(ID, order.getId());
            intent.putExtra(DATE, order.getDate());
            startActivityForResult(intent, RATE_CLIENT);
        }else{
            startActivity(new Intent(this, OrderDetailsActivity.class).putExtra(ID,order.getId()));
        }
    }

    private void onFinishedOrdersResponse(OrdersResponse response) {
        finishedOrdersAdapter.setData(response.getData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RATE_CLIENT && resultCode == Activity.RESULT_OK)
            mViewModel.requestFinishedOrders();

    }
}
