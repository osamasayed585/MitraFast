package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FAQ;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemFaqBinding;

import java.util.ArrayList;
import java.util.List;

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.FAQsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<FAQ> mData = new ArrayList<>();
    private FAQsAdapterOnClickHandler mClickHandler;
    Context context;

    public FAQsAdapter() {
    }

    public void setItemClickListener(FAQsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public FAQsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_faq, parent, false);
        return new FAQsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQsAdapterViewHolder holder, int position) {
        FAQ faq = mData.get(position);
        holder.mBinding.tvQuestion.setText(faq.getQuestion());
        holder.mBinding.tvAnswer.setText(faq.getAnswer());
        holder.mBinding.tvQuestion.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, faq.is_visible() ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow, 0);
        holder.mBinding.cvAnswer.setVisibility(faq.is_visible() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<FAQ> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface FAQsAdapterOnClickHandler {
        void onFAQItemClick(FAQ faq);
    }

    public class FAQsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemFaqBinding mBinding;

        public FAQsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemFaqBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            mData.get(getBindingAdapterPosition()).inverseVisibility();
            notifyItemChanged(getBindingAdapterPosition());
        }
    }
}