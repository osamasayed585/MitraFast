package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemCartProductBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.CartProductsAdapterViewHolder> {

    Context context;
    //The list of Objects that will populate the RecyclerView
    private List<CartProduct> mData = new ArrayList<>();
    // ClickHandler Build
    private CartProductsAdapterOnClickHandler mClickHandler;

    public CartProductsAdapter() {
    }

    public void setDeleteClickListener(CartProductsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public CartProductsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cart_product, parent, false);
        return new CartProductsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductsAdapterViewHolder holder, int position) {
        CartProduct cartProduct = mData.get(position);
        Picasso.get().load(cartProduct.getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(holder.mBinding.ivProductImage);
        holder.mBinding.productName.setText(cartProduct.getName());
        holder.mBinding.tvType.setText(cartProduct.getType_name());
        holder.mBinding.tvSize.setText(cartProduct.getSize_name());
        holder.mBinding.tvExtras.setText(cartProduct.getExtras_names());
        holder.mBinding.tvChoices.setText(cartProduct.getChoices_names());
        holder.mBinding.additionsView.setVisibility(cartProduct.getExtras_names().equals("[]") ? View.GONE : View.VISIBLE);
        holder.mBinding.optionsView.setVisibility(cartProduct.getChoices_names().equals("[]") ? View.GONE : View.VISIBLE);
        if(cartProduct.getType_name()==null||cartProduct.getType_name().equals("")){
            holder.mBinding.typeView.setVisibility(View.GONE);
        }
        if(cartProduct.getSize_name()==null||cartProduct.getSize_name().equals("")){
            holder.mBinding.sizeView.setVisibility(View.GONE);
        }
        if(cartProduct.getChoices_names()==null||cartProduct.getChoices_names().equals("")){
            holder.mBinding.optionsView.setVisibility(View.GONE);
        }
        if(cartProduct.getExtras_names()==null||cartProduct.getExtras_names().equals("")){
            holder.mBinding.additionsView.setVisibility(View.GONE);
        }


        holder.mBinding.cost.setText(String.format("%s %s", (cartProduct.getCost() * cartProduct.getQuantity()), cartProduct.getCurrency()));
        holder.mBinding.count.setText(String.valueOf(cartProduct.getQuantity()));

    }


    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public List<CartProduct> getData() {
        return mData;
    }

    public void setData(List<CartProduct> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface CartProductsAdapterOnClickHandler {
        void onProductDeleteClick(int productId);

        void onPlusOrMinusClick(int productPosition, int currentCount);


    }

    public class CartProductsAdapterViewHolder extends RecyclerView.ViewHolder {

        //get an Instance for dataBinding
        ListItemCartProductBinding mBinding;


        public CartProductsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemCartProductBinding.bind(itemView);
            mBinding.remove.setOnClickListener(this::onRemoveFromCart);


            mBinding.plus.setOnClickListener(this::onPlusClick);
            mBinding.minus.setOnClickListener(this::onMinusClick);
        }

        public void onRemoveFromCart(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            CartProduct cartProduct = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onProductDeleteClick(cartProduct.getId());


        }

        public void onPlusClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (mClickHandler != null)
                mClickHandler.onPlusOrMinusClick(mData.get(adapterPosition).getId(), mData.get(adapterPosition).getQuantity() + 1);

        }

        public void onMinusClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked

            if (Integer.parseInt(mBinding.count.getText().toString()) > 1) {
                int currentCount = mData.get(adapterPosition).getQuantity() - 1;
                if (mClickHandler != null)
                    mClickHandler.onPlusOrMinusClick(mData.get(adapterPosition).getId(), currentCount);
            }
        }
    }


}
