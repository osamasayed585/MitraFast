package com.linkpcom.mitrafast.MVVM.Client.Coupons;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.CouponsAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.AddCouponDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityCouponsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CouponsActivity extends BaseActivity {

    ActivityCouponsBinding mBinding;

    CouponsViewModel mViewModel;

    AddCouponDialog addCouponDialog;
    private CouponsAdapter couponsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCouponsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(CouponsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        //Request And Observer
        mViewModel.getCouponsResponseMutableLiveData().observe(this, this::onCouponsResponse);

        //ViewPager Setup
        couponsAdapter = new CouponsAdapter();

        mBinding.rvCoupons.setAdapter(couponsAdapter);

        addCouponDialog = new AddCouponDialog(this);

//        mBinding.ivAdd.setOnClickListener(this::onAddClick);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestCoupons();

    }

    private void onAddClick(View view) {
        addCouponDialog.getDialog().show();
        addCouponDialog.setAddCardListDialogHandler(code -> {
            mViewModel.requestAddCoupon(code);

        });
    }

    private void onCouponsResponse(CouponsResponse response) {
        couponsAdapter.setData(response.getData());
        if (response.getData().isEmpty()) {
            mBinding.tvNoProducts.setVisibility(View.VISIBLE);
        }

    }


}