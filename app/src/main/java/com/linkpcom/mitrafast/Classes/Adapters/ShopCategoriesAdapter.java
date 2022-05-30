package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemShopCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopCategoriesAdapter extends RecyclerView.Adapter<ShopCategoriesAdapter.ShopCategoriesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Category> mData = new ArrayList<>();
    private ShopCategoriesAdapterOnClickHandler mClickHandler;
    Context context;

    int selectedPosition;

    public ShopCategoriesAdapter() {
    }

    public void setItemClickListener(ShopCategoriesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ShopCategoriesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_shop_category, parent, false);
        return new ShopCategoriesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCategoriesAdapterViewHolder holder, int position) {
        Category shopCategory = mData.get(position);
        holder.mBinding.categoryName.setText(shopCategory.getName());

        holder.mBinding.backView.setBackgroundTintList(position == selectedPosition ? ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)) : ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
        holder.mBinding.categoryName.setTextColor(context.getResources().getColor(position == selectedPosition ? R.color.white : R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Category> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<Category> getData() {
        return mData;
    }

    //The interface that will be implemented later in parent activity
    public interface ShopCategoriesAdapterOnClickHandler {
        void onShopCategoryItemClick(Category shopCategory);
    }

    public class ShopCategoriesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemShopCategoryBinding mBinding;

        public ShopCategoriesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemShopCategoryBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            selectedPosition = adapterPosition;
            notifyDataSetChanged();
            //Data passed when clicked
            Category shopCategory = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onShopCategoryItemClick(shopCategory);
        }
    }
}