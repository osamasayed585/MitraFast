package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Offer;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemOfferBinding;

import java.util.ArrayList;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Offer> mData = new ArrayList<>();
    private OffersAdapterOnClickHandler mClickHandler;
    Context context;

    public OffersAdapter() {
    }

    public void setItemClickListener(OffersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public OffersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_offer, parent, false);
        return new OffersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapterViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.mBinding.ivImage);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Offer> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface OffersAdapterOnClickHandler {
        void onOfferItemClick(Offer offer);
    }

    public class OffersAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemOfferBinding mBinding;

        public OffersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemOfferBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Offer offer = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onOfferItemClick(offer);
        }
    }
}