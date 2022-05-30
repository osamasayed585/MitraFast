package com.linkpcom.mitrafast.MVVM.Client.Bills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.BillsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.BillDetails.BillDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityBillsBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.RATE_AGENT;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class BillsActivity extends BaseActivity {
    ActivityBillsBinding mBinding;

    BillsViewModel mViewModel;

    BillsAdapter billsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBillsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(BillsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getBillsResponseMutableLiveData().observe(this, this::onBillsResponse);

        //New Instances
        billsAdapter = new BillsAdapter();

        billsAdapter.setItemClickListener(this::onBillClick);

        mBinding.rvBills.setAdapter(billsAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestBills();
    }

    private void onBillClick(Order order) {
        startActivity(new Intent(this, BillDetailsActivity.class));

    }

    private void onBillsResponse(OrdersResponse response) {
        billsAdapter.setData(response.getData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RATE_AGENT && resultCode == Activity.RESULT_OK)
            mViewModel.requestBills();


    }
}
