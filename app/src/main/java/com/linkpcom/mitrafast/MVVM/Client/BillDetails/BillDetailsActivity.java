package com.linkpcom.mitrafast.MVVM.Client.BillDetails;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CompanyProfile;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CompanyProfileResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityBillDetailsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BillDetailsActivity extends BaseActivity {
    ActivityBillDetailsBinding mBinding;

    Order order;
    private BillDetailsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBillDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        viewModel = new ViewModelProvider(this).get(BillDetailsViewModel.class);
        viewModel.requestCompanyProfile().observe(this, this::onProfileResponse);
        order = getIntent().getParcelableExtra(OBJECT);


        fillData();
    }

    private void onProfileResponse(CompanyProfileResponse companyProfileResponse) {
        CompanyProfile companyProfile = companyProfileResponse.getData();
        mBinding.tvAddress.setText(companyProfile.getAddress());
        mBinding.tvName.setText(companyProfile.getCompany_name());
        mBinding.tvVatIdentificationNo.setText(companyProfile.getTax_id_number());

    }

    private void fillData() {
        mBinding.tvInvoiceNumber.setText(String.valueOf(order.getId()));
        mBinding.tvInvoiceDate.setText(order.getCreated_at().substring(0, 10));
        mBinding.tvDeliveryFees.setText(String.format("%s %s", order.getDelivery_cost_with_app_percentage_value(), order.getCurrency()));
        mBinding.tvSubTotalVatIncluded.setText(String.format("%s %s", order.getDelivery_cost_with_tax().substring(0, 4), order.getCurrency()));
        mBinding.tvVat.setText(String.format("%s %%", order.getDelivery_cost_tax_percentage()));
        if (order.getCoupon_discount_cost() != null && order.getCoupon_discount_percentage() != null) {
            mBinding.tvCouponDiscountCost.setText(String.format("%s %s", order.getCoupon_discount_cost(), order.getCurrency()));
            mBinding.tvCouponDiscountPercentage.setText(String.format("%s %%", order.getCoupon_discount_percentage()));
            mBinding.discount.setVisibility(View.VISIBLE);
        } else mBinding.discount.setVisibility(View.GONE);
        mBinding.tvVatAmount.setText(String.format("%s %s", order.getDelivery_cost_tax_percentage_value(), order.getCurrency()));
    }


}
