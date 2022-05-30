package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Transaction;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemPaymentCardBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class PaymentCardsAdapter extends RecyclerView.Adapter<PaymentCardsAdapter.PaymentCardsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<PaymentCard> mData;
    private PaymentCardsAdapterOnClickHandler mClickHandler;
    public PublishSubject<Integer> onDeleteCard = PublishSubject.create();
    private Context context;

    public PaymentCardsAdapter() {
    }

    public void setItemClickListener(PaymentCardsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public PaymentCardsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_payment_card, parent, false);
        return new PaymentCardsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCardsAdapterViewHolder holder, int position) {
        holder.mBinding.tvNumber.setText(mData.get(position).getCredit_card_x_number());
        holder.mBinding.tvExpiryDate.setText(mData.get(position).getExpiry_date());
        holder.mBinding.tvName.setText(mData.get(position).getCredit_card_name());

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<PaymentCard> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public class PaymentCardsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemPaymentCardBinding mBinding;

        public PaymentCardsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemPaymentCardBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
            mBinding.btnDelete.setOnClickListener(view -> {
                onDeleteCard.onNext(mData.get(getBindingAdapterPosition()).getId());
            });
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            PaymentCard paymentCard = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onPaymentCardItemClick(paymentCard);
        }

    }

    //The interface that will be implemented later in parent activity
    public interface PaymentCardsAdapterOnClickHandler {
        void onPaymentCardItemClick(PaymentCard paymentCard);
    }
}