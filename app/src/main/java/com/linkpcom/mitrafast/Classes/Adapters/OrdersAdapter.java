package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Order> mData = new ArrayList<>();
    private OrdersAdapterOnClickHandler mClickHandler;
    Context context;

    public OrdersAdapter() {
    }

    public void setItemClickListener(OrdersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public OrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, parent, false);
        return new OrdersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapterViewHolder holder, int position) {
        Order order = mData.get(position);
        Picasso.get().load(order.getShop().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.shopImage);
        holder.mBinding.shopName.setText(order.getShop().getName());
        holder.mBinding.orderId.setText(String.format("#%s", order.getId()));
        holder.mBinding.orderDate.setText(order.getDate());
        holder.mBinding.orderStatus.setText(order.getOrder_status().getMessage());
        if (order.getLocation() != null)
            holder.mBinding.orderAddress.setText(order.getLocation().getAddress());

        if (order.getCancellation_reason() != null) {
            holder.mBinding.container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E24C4B")));
            holder.mBinding.action.setText(String.format("%s : %s", context.getString(R.string.cancelation_reason), order.getCancellation_reason().getName()));
            holder.mBinding.action.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_order_canceled, 0, R.drawable.ic_forward_arrow, 0);
            holder.itemView.setEnabled(false);
        } else {
            if (order.getOrder_status().getId() == 7) {
                holder.mBinding.container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#32CD32")));
                holder.mBinding.track.setVisibility(View.GONE);
            } else
                holder.mBinding.container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BA6E2")));
            holder.mBinding.action.setText(context.getString(R.string.track_order));

            holder.mBinding.action.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_tracking, 0, R.drawable.ic_forward_arrow, 0);
            holder.itemView.setEnabled(true);
        }
//        holder.mBinding.tvPrice.setText(String.format("%s%s", order.getCost(), order.getCurrency()));

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
    public interface OrdersAdapterOnClickHandler {
        void onOrderItemClick(Order order);
    }

    public class OrdersAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemOrderBinding mBinding;

        public OrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemOrderBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Order order = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onOrderItemClick(order);
        }
    }
}