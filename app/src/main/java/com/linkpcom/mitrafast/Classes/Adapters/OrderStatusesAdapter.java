package com.linkpcom.mitrafast.Classes.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Status;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemOrderStatusBinding;

import java.util.List;


public class OrderStatusesAdapter extends RecyclerView.Adapter<OrderStatusesAdapter.OrderStatusesAdapterViewHolder> {

    private List<Status> mData;
    private int selectedItemPosition;
    private final int Just_One_Item_At_RecyclerView = 1;
    private OrderStatusesAdapterOnClickHandler mClickHandler;
    private OnStatusChangeListener onStatusChangeListener;

    public OrderStatusesAdapter() {
        selectedItemPosition = 0;
    }

    public static class OrderStatusesAdapterViewHolder extends RecyclerView.ViewHolder {

        private ListItemOrderStatusBinding mBinding;

        public OrderStatusesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemOrderStatusBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public OrderStatusesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderStatusesAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order_status, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusesAdapterViewHolder holder, int position) {
        if (mData.size() == 0) {
            mClickHandler.isStatusFinished(true);
        } else if (!mData.isEmpty())
            holder.mBinding.optionName.setText(mData.get(0).getName());
    }

    @Override
    public int getItemCount() {
        return Just_One_Item_At_RecyclerView;
    }

    public void setData(List<Status> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setSelectedItemPosition(int position) {
        if (mData.size() == 0) {
            mClickHandler.isStatusFinished(true);
        } else {
            selectedItemPosition = position; // 1
            onStatusChangeListener.isStatusChange(mData.get(0).getId()); // web service
            notifyDataSetChanged();
        }
    }

    public int getSelectedItemId() {
        return mData.get(selectedItemPosition).getId();
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public interface OrderStatusesAdapterOnClickHandler {
        void isStatusFinished(boolean status);
    }

    public interface OnStatusChangeListener {
        void isStatusChange(int orderStatusId);
    }

    public void initStatusAgent(OnStatusChangeListener onStatusChangeListener) {
        this.onStatusChangeListener = onStatusChangeListener;
    }

    public void initialItemClickListener(OrderStatusesAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }
}