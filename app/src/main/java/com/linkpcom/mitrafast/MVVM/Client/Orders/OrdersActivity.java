package com.linkpcom.mitrafast.MVVM.Client.Orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.OrdersAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Client.RateAgent.RateAgentActivity;
import com.linkpcom.mitrafast.databinding.ActivityClientOrdersBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.DATE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.RATE_AGENT;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrdersActivity extends BaseActivity {
    ActivityClientOrdersBinding mBinding;

    OrdersViewModel mViewModel;

    OrdersAdapter ordersAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientOrdersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getOrdersResponseMutableLiveData().observe(this, this::onOrdersResponse);

        //New Instances
        ordersAdapter = new OrdersAdapter();

        ordersAdapter.setItemClickListener(this::onOrderClick);

        mBinding.orders.setAdapter(ordersAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestOrders();
    }

    private void onOrderClick(Order order) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(ID, order.getId());
        startActivity(intent);
    }

    private void onOrdersResponse(OrdersResponse response) {
        ordersAdapter.setData(response.getData());
        mBinding.noOrdersHolder.setVisibility(ordersAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RATE_AGENT && resultCode == Activity.RESULT_OK)
            mViewModel.requestOrders();

    }
}
