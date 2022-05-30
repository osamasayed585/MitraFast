package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Extra;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemExtraBinding;

import java.util.ArrayList;
import java.util.List;

public class ExtrasAdapter extends RecyclerView.Adapter<ExtrasAdapter.ExtrasAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Extra> mData = new ArrayList<>();
    private ExtrasAdapterOnClickHandler mClickHandler;
    Context context;
    private double totalCost;
    private String currency;

    public ExtrasAdapter() {
    }

    public void setItemClickListener(ExtrasAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ExtrasAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_extra, parent, false);
        return new ExtrasAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtrasAdapterViewHolder holder, int position) {
        Extra extra = mData.get(position);
        holder.mBinding.extra.setText(extra.getName());
        holder.mBinding.cost.setText(String.format("%s%s", extra.getPrice(), currency));
        holder.mBinding.extra.setBackgroundResource(extra.isChecked() ? R.drawable.rounded_orange_solid : R.drawable.rounded_orange_stroke);
        holder.mBinding.extra.setTextColor(context.getResources().getColor(extra.isChecked()? R.color.white : R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Extra> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<Integer> getSelectedDataIdes() {
        List<Integer> extras = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked())
                extras.add(mData.get(i).getId());
        }
        return extras;
    }

    public List<String> getSelectedDataNames() {
        List<String> extras = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked())
                extras.add(mData.get(i).getName());
        }
        return extras;
    }

    //The interface that will be implemented later in parent activity
    public interface ExtrasAdapterOnClickHandler {
        void onExtraItemClick(double totalCost);
    }

    public class ExtrasAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemExtraBinding mBinding;

        public ExtrasAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemExtraBinding.bind(itemView);
            mBinding.extra.setOnClickListener(this::OnExtraClick);
        }

        private void OnExtraClick(View view) {
            mData.get(getAdapterPosition()).reverseCheck();
            totalCost = mData.get(getAdapterPosition()).isChecked() ? totalCost + mData.get(getAdapterPosition()).getPrice() : totalCost - mData.get(getAdapterPosition()).getPrice();
            if (mClickHandler != null)
                mClickHandler.onExtraItemClick(totalCost);
            notifyDataSetChanged();

        }


    }
}