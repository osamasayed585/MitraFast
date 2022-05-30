package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemBillBinding;

import java.util.ArrayList;
import java.util.List;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Order> mData = new ArrayList<>();
    private BillsAdapterOnClickHandler mClickHandler;
    Context context;

    public BillsAdapter() {
    }

    public void setItemClickListener(BillsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public BillsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bill, parent, false);
        return new BillsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsAdapterViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getShop().getImage()).into(holder.mBinding.ivImage);
        holder.mBinding.tvName.setText(mData.get(position).getShop().getName());
        holder.mBinding.tvOrderNumber.setText(String.valueOf(mData.get(position).getId()));
        holder.mBinding.tvDate.setText(mData.get(position).getDate());
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

    //The interface that will be implemented later in parent activity
    public interface BillsAdapterOnClickHandler {
        void onBillItemClick(Order order);
    }

    public class BillsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemBillBinding mBinding;

        public BillsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemBillBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Order order = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onBillItemClick(order);
        }
    }
}