package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Size;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemSizeBinding;

import java.util.ArrayList;
import java.util.List;

public class SizesAdapter extends RecyclerView.Adapter<SizesAdapter.SizesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Size> mData = new ArrayList<>();
    private SizesAdapterOnClickHandler mClickHandler;
    Context context;
    private String currency;
    private int selectedSizePosition;

    public SizesAdapter() {
    }

    public void setItemClickListener(SizesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SizesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_size, parent, false);
        return new SizesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizesAdapterViewHolder holder, int position) {
        Size size = mData.get(position);
        holder.mBinding.size.setText(size.getName());
        holder.mBinding.cost.setText(String.format("%s%s", size.getPrice(), currency));
        holder.mBinding.size.setBackgroundResource(selectedSizePosition == position ? R.drawable.rounded_orange_solid : R.drawable.rounded_orange_stroke);
        holder.mBinding.size.setTextColor(context.getResources().getColor(selectedSizePosition == position ? R.color.white : R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Size> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    //The interface that will be implemented later in parent activity
    public interface SizesAdapterOnClickHandler {
        void onSizeItemClick(Size size);
    }

    public class SizesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemSizeBinding mBinding;

        public SizesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemSizeBinding.bind(itemView);
            mBinding.size.setOnClickListener(this::OnSizeClick);
        }

        private void OnSizeClick(View view) {
            selectedSizePosition = getAdapterPosition();
            notifyDataSetChanged();
            if (mClickHandler != null)
                mClickHandler.onSizeItemClick(mData.get(selectedSizePosition));
        }




    }
}