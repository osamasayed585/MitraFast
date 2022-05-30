package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.OrderContent;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemOrderContentBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderContentsAdapter extends RecyclerView.Adapter<OrderContentsAdapter.OrderContentsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<OrderContent> mData = new ArrayList<>();
    private OrderContentsAdapterOnClickHandler mClickHandler;
    private Context context;

    public OrderContentsAdapter() {
    }

    public void setItemClickListener(OrderContentsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public OrderContentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order_content, parent, false);
        return new OrderContentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderContentsAdapterViewHolder holder, int position) {
        OrderContent orderContent = mData.get(position);
        holder.mBinding.orderContent.setText(orderContent.getName());
        holder.mBinding.orderCount.setText(String.format("%s  %s", context.getResources().getString(R.string.count), orderContent.getCount()));
        Picasso.get().load(orderContent.getImage()).into(holder.mBinding.ivRestaurant);
        String orderSpecifications = getOrderSpecifications(orderContent);
        holder.mBinding.orderSpecifications.setText(orderSpecifications);
    }

    @NotNull
    private String getOrderSpecifications(OrderContent orderContent) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!orderContent.getType().isEmpty())
            stringBuilder.append("   ").append(context.getString(R.string.type)).append(" : ").append(orderContent.getType()).append("\n");
        if (!orderContent.getSize().isEmpty())
            stringBuilder.append("   ").append(context.getString(R.string.size)).append(" : ").append(orderContent.getSize()).append("\n");
        if (!orderContent.getExtras().isEmpty())
            stringBuilder.append("   ").append(context.getString(R.string.extras)).append(" : ").append(orderContent.getExtras()).append("\n");
        if (!orderContent.getChoices().isEmpty())
            stringBuilder.append("   ").append(context.getString(R.string.choices)).append(" : ").append(orderContent.getChoices()).append("\n");
//        stringBuilder.setLength(stringBuilder.length() - 2);
        String data = stringBuilder.toString();
        String substring = data;
        if (data.length() > 0)
            substring = data.substring(0, data.length() - 1);

        return substring;
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<OrderContent> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface OrderContentsAdapterOnClickHandler {
        void onOrderContentItemClick(OrderContent orderContent);
    }

    public class OrderContentsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemOrderContentBinding mBinding;

        public OrderContentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemOrderContentBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            OrderContent orderContent = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onOrderContentItemClick(orderContent);
        }
    }
}