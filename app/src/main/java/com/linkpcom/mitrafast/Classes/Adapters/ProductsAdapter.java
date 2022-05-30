package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Product;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductCountResponse;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemProductBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Product> mData = new ArrayList<>();
    private ProductsAdapterOnClickHandler mClickHandler;
    Context context;

    public ProductsAdapter() {
    }

    public void setItemClickListener(ProductsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ProductsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false);
        return new ProductsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapterViewHolder holder, int position) {
        Product product = mData.get(position);

        if (product.getImage() != null)
            Picasso.get().load(product.getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.ivProduct);
        else
            holder.mBinding.ivProduct.setImageResource(R.color.dark_gray);
        holder.mBinding.tvProductName.setText(product.getProduct_name());
        holder.mBinding.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.mBinding.tvCurrency.setText(product.getCurrency());
        holder.mBinding.tvProductDescription.setText(product.getDescription());
        holder.mBinding.tvCalories.setText(product.getCal());
        holder.mBinding.tvCount.setText(String.valueOf(product.getCount()));
        holder.mBinding.tvCount.setVisibility(product.getCount() == 0 ? View.GONE : View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Product> data) {
        if (mData == null)
            mData = data;
        else
            mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public List<Product> getData() {
        return mData;
    }

    public void setProductCount(ProductCountResponse response) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == response.getId()) {
                mData.get(i).setCount(response.getCount());
                notifyItemChanged(i);
                break;
            }
        }
    }

    //The interface that will be implemented later in parent activity
    public interface ProductsAdapterOnClickHandler {
        void onProductItemClick(Product product);
    }

    public class ProductsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemProductBinding mBinding;

        public ProductsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemProductBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Product product = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onProductItemClick(product);
        }
    }
}