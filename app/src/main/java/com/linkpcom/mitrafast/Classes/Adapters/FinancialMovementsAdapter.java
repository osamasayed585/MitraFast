package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemFinancialMovementBinding;

import java.util.ArrayList;
import java.util.List;

public class FinancialMovementsAdapter extends RecyclerView.Adapter<FinancialMovementsAdapter.FinancialMovementsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Order> mData = new ArrayList<>();
    private FinancialMovementsAdapterOnClickHandler mClickHandler;
    Context context;

    public FinancialMovementsAdapter() {
    }

    public void setItemClickListener(FinancialMovementsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public FinancialMovementsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_financial_movement, parent, false);
        return new FinancialMovementsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancialMovementsAdapterViewHolder holder, int position) {
        Order financialMovement = mData.get(position);
        holder.mBinding.orderId.setText(String.format("#%s", financialMovement.getId()));
        holder.mBinding.tvTotalPurchases.setText(String.format("%s%s", financialMovement.getTotal_cost(), financialMovement.getCurrency()));
        holder.mBinding.tvServiceValue.setText(String.format("%s%s", financialMovement.getDelivery_cost(), financialMovement.getCurrency()));
        holder.mBinding.tvCommission.setText(String.format("%s%s", financialMovement.getApp_percentage(), financialMovement.getCurrency()));
        holder.mBinding.rating.setStar(financialMovement.getRate().getClient_rate() != null ? Float.parseFloat(financialMovement.getRate().getClient_rate()) : 0);
        holder.mBinding.tvPaymentType.setText(financialMovement.getPayment_type().getName());
        holder.mBinding.tvDate.setText(financialMovement.getDate());

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Order> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public String getIds() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            ids.add(mData.get(i).getId());
        }
        return ids.toString();
    }

    //The interface that will be implemented later in parent activity
    public interface FinancialMovementsAdapterOnClickHandler {
        void onFinancialMovementItemClick(Order financialMovement);
    }

    public class FinancialMovementsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemFinancialMovementBinding mBinding;

        public FinancialMovementsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemFinancialMovementBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Order financialMovement = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onFinancialMovementItemClick(financialMovement);
        }
    }
}