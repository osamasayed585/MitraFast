package com.linkpcom.mitrafast.Classes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.Utils.OrderLiveStatus;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainViewModel;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.RowOrderStatusBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StatusOrderAdapter extends RecyclerView.Adapter<StatusOrderAdapter.MyOrderStatus> {

    private List<Order> mOrder;
    private OnOrderStatusListener listener;
    private OnOrderStatusClicked onOrderClick;
    private MainViewModel viewModel;

    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public StatusOrderAdapter() {
    }

    public void setData(List<Order> mOrder) {
        this.mOrder = mOrder;
        notifyDataSetChanged();
    }

    public void initListener(OnOrderStatusListener listener) {
        this.listener = listener;
    }

    public void initOrderClick(OnOrderStatusClicked onOrderClick) {
        this.onOrderClick = onOrderClick;
    }

    @NonNull
    @Override
    public MyOrderStatus onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_status, parent, false);
        return new MyOrderStatus(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderStatus holder, int position) {
        holder.bind(mOrder.get(position));
        int id = mOrder.get(position).getId();
        OrderLiveStatus orderLiveStatus = new OrderLiveStatus(id);
        orderLiveStatus.setStatusChanged(() -> listener.onOrderStatusChanged(id));
    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    public class MyOrderStatus extends RecyclerView.ViewHolder {

        private RowOrderStatusBinding mBinding;


        public MyOrderStatus(@NonNull View itemView) {
            super(itemView);
            mBinding = RowOrderStatusBinding.bind(itemView);
            itemView.setOnClickListener(this::onOrderClicked);
        }

        private void onOrderClicked(View view) {
            if (listener != null) {
                onOrderClick.onOrderStatusClicked(mOrder.get(getBindingAdapterPosition()));
            }
        }

        private void bind(Order order) {
            Picasso.get().load(order.getShop().getImage()).placeholder(R.drawable.category_place_holder).error(R.drawable.category_place_holder).into(mBinding.ivProfile);
            mBinding.tvStoreName.setText(order.getShop().getName());
            mBinding.tvNumOrder.setText(String.format("%s", order.getId()));
            mBinding.tvOrderStatus.setText(order.getOrder_status().getName());
        }
    }

    public interface OnOrderStatusListener {
        void onOrderStatusChanged(int id);
    }


    public interface OnOrderStatusClicked {
        void onOrderStatusClicked(Order order);
    }
}
