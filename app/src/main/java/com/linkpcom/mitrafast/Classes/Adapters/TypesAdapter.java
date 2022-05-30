package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Type;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemTypeBinding;

import java.util.ArrayList;
import java.util.List;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.TypesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Type> mData = new ArrayList<>();
    private TypesAdapterOnClickHandler mClickHandler;
    Context context;
    private String currency;
    private int selectedTypePosition;

    public TypesAdapter() {
    }

    public void setItemClickListener(TypesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TypesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_type, parent, false);
        return new TypesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypesAdapterViewHolder holder, int position) {
        Type type = mData.get(position);
        holder.mBinding.type.setText(type.getName());
        holder.mBinding.cost.setText(String.format("%s%s", type.getPrice(), currency));
        holder.mBinding.type.setBackgroundResource(selectedTypePosition == position ? R.drawable.rounded_orange_solid : R.drawable.rounded_orange_stroke);
        holder.mBinding.type.setTextColor(context.getResources().getColor(selectedTypePosition == position ? R.color.white : R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Type> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    //The interface that will be implemented later in parent activity
    public interface TypesAdapterOnClickHandler {
        void onTypeItemClick(Type type);
    }

    public class TypesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemTypeBinding mBinding;

        public TypesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemTypeBinding.bind(itemView);
            mBinding.type.setOnClickListener(this::OnTypeClick);
        }

        private void OnTypeClick(View view) {
            selectedTypePosition = getAdapterPosition();
            notifyDataSetChanged();
            if (mClickHandler != null)
                mClickHandler.onTypeItemClick(mData.get(selectedTypePosition));
        }


    }
}