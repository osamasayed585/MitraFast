package com.linkpcom.mitrafast.Classes.Adapters;

import static com.linkpcom.mitrafast.Classes.Utils.Utils.convertTime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Coupon;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemCouponBinding;

import java.util.ArrayList;
import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.CouponsAdapterViewHolder> {
    private static final int WALLET_BALANCE = 2;
    private static final int DISCOUNT = 1;
    //The list of Objects that will populate the RecyclerView
    private List<Coupon> mData = new ArrayList<>();
    private CouponsAdapterOnClickHandler mClickHandler;
    Context context;

    public CouponsAdapter() {
    }

    public void setItemClickListener(CouponsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CouponsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_coupon, parent, false);
        return new CouponsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponsAdapterViewHolder holder, int position) {
        if (mData.get(position).getVoucher() != null) {
            holder.mBinding.tvCouponNumber.setText(mData.get(position).getVoucher().getCode());
            holder.mBinding.tvExpiryDate.setText(convertTime(Long.parseLong(mData.get(position).getVoucher().getExpires_at() + "000")));
            holder.mBinding.tvCouponType.setText(mData.get(position).getVoucher().getDescription());


            if (mData.get(position).getVoucher().getType().getId() == WALLET_BALANCE) {
                holder.mBinding.type.setText(context.getString(R.string.add));
                holder.mBinding.tvDiscountValue.setText(String.format("%s %s", mData.get(position).getVoucher().getMaximum_discount(), context.getResources().getString(R.string.Rial)));
            } else if (mData.get(position).getVoucher().getType().getId() == DISCOUNT)
                holder.mBinding.tvDiscountValue.setText(String.format("%s%%", mData.get(position).getVoucher().getMaximum_discount()));
        }
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Coupon> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface CouponsAdapterOnClickHandler {
        void onCouponItemClick(Coupon coupon);
    }

    public class CouponsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemCouponBinding mBinding;

        public CouponsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemCouponBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Coupon coupon = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onCouponItemClick(coupon);
        }
    }
}