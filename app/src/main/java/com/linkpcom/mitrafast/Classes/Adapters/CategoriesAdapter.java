package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Category> mData = new ArrayList<>();
    private CategoriesAdapterOnClickHandler mClickHandler;
    Context context;

    public CategoriesAdapter() {
    }

    public void setItemClickListener(CategoriesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public CategoriesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
        return new CategoriesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapterViewHolder holder, int position) {
        Category category = mData.get(position);
        Picasso.get().load(category.getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.ivCategoryImageRight);
        holder.mBinding.tvCategoryNameRight.setText(category.getName());
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

    //The interface that will be implemented later in parent activity
    public interface CategoriesAdapterOnClickHandler {
        void onCategoryItemClick(Category category);
    }

    public class CategoriesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemCategoryBinding mBinding;

        public CategoriesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemCategoryBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Category category = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onCategoryItemClick(category);
        }
    }
}