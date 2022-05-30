package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Transaction;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemTransactionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Transaction> mData = new ArrayList<>();
    private TransactionsAdapterOnClickHandler mClickHandler;
    Context context;
    private String currency;

    public TransactionsAdapter() {
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        notifyDataSetChanged();
    }

    public void setItemClickListener(TransactionsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TransactionsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false);
        return new TransactionsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapterViewHolder holder, int position) {
        holder.mBinding.tvName.setText(mData.get(position).getReason());
        holder.mBinding.tvAddDate.setText(mData.get(position).getCreated_at());
        if (mData.get(position).getOrder() != null)
            holder.mBinding.tvOrderNumber.setText(String.valueOf(mData.get(position).getOrder().getId()));
        else  holder.mBinding.tvOrderNumber.setText("---");
        holder.mBinding.tvValue.setText(String.format(Locale.ENGLISH, "%.2f %s",
                Double.parseDouble(mData.get(position).getValue()), currency));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Transaction> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface TransactionsAdapterOnClickHandler {
        void onTransactionItemClick(Transaction transaction);
    }

    public class TransactionsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemTransactionBinding mBinding;

        public TransactionsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemTransactionBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Transaction transaction = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onTransactionItemClick(transaction);
        }
    }
}