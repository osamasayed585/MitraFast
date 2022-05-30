package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Support;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemFaqBinding;
import com.linkpcom.mitrafast.databinding.ListItemSupportBinding;

import java.util.ArrayList;
import java.util.List;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.SupportAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Support> mData = new ArrayList<>();
    private SupportAdapterOnClickHandler mClickHandler;
    Context context;

    public SupportAdapter() {
    }

    public void setItemClickListener(SupportAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SupportAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_support, parent, false);
        return new SupportAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapterViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.mBinding.ivImage);
        holder.mBinding.tvName.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Support> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface SupportAdapterOnClickHandler {
        void onSupportItemClick(Support faq);
    }

    public class SupportAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemSupportBinding mBinding;

        public SupportAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemSupportBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            if (mClickHandler != null)
                mClickHandler.onSupportItemClick(mData.get(getBindingAdapterPosition()));

        }
    }
}