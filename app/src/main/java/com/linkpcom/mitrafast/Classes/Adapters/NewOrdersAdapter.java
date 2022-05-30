package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemNewOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class NewOrdersAdapter extends RecyclerView.Adapter<NewOrdersAdapter.NewOrdersAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Order> mData = new ArrayList<>();
    private NewOrdersAdapterOnClickHandler mClickHandler;
    Context context;
    LatLng currentLocation;

    public NewOrdersAdapter() {
    }

    public void setData(List<Order> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addOrder(Order data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void removeOrder(String orderId) {
        for (int i = 0; i < mData.size(); i++) {
            if (String.valueOf(mData.get(i).getId()).equals(orderId)) {
                mData.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setItemClickListener(NewOrdersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NewOrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_new_order, parent, false);
        return new NewOrdersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrdersAdapterViewHolder holder, int position) {
        Order newOrder = mData.get(position);
        Picasso.get().load(newOrder.getShop().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.ivProfilePic);
        holder.mBinding.shopName.setText(newOrder.getShop().getName());


    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
        notifyDataSetChanged();
    }

    public interface NewOrdersAdapterOnClickHandler {
        void onOrderDetailsClick(Order newOrder);

    }

    public class NewOrdersAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemNewOrderBinding mBinding;

        public NewOrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemNewOrderBinding.bind(itemView);
            itemView.setOnClickListener(this::onDetailsClicked);

        }


        private void onDetailsClicked(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Order newOrder = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onOrderDetailsClick(newOrder);
        }


    }


}