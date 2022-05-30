package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentMethod;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemPaymentMethodBinding;

import java.util.ArrayList;
import java.util.List;


public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodsAdapterViewHolder> {

    //The list of Objects that will populate the RecyclerView
    private List<PaymentMethod> mData = new ArrayList<>();

    // ClickHandler Build
    private PaymentMethodsAdapterOnClickHandler mClickHandler;

    public PaymentMethodsAdapter() {
    }

    public void setItemClickListener(PaymentMethodsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public PaymentMethodsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_payment_method;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PaymentMethodsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodsAdapterViewHolder holder, int position) {
//        holder.mBinding.paymentMethodName.setText(mData.get(position).getName());
        Picasso.get().load(mData.get(position).getImage()).placeholder(R.mipmap.main_logo).error(R.mipmap.main_logo).into(holder.mBinding.paymentMethodImage);
        holder.mBinding.tvName.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<PaymentMethod> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface PaymentMethodsAdapterOnClickHandler {
        void onPaymentMethodItemClick(PaymentMethod paymentMethod);
    }

    public class PaymentMethodsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //get an Instance for dataBinding
        ListItemPaymentMethodBinding mBinding;


        public PaymentMethodsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemPaymentMethodBinding.bind(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            PaymentMethod paymentMethod = mData.get(adapterPosition);
            mClickHandler.onPaymentMethodItemClick(paymentMethod);
        }

    }


}
