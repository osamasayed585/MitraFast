package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CancelReason;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemDeleteOrderReasonBinding;

import java.util.ArrayList;
import java.util.List;


public class DeleteOrderDialogAdapter extends RecyclerView.Adapter<DeleteOrderDialogAdapter.DeleteOrderDialogAdapterViewHolder> {
    int selectedItemPosition;
    //The list of Objects that will populate the RecyclerView
    private List<CancelReason> mData = new ArrayList<>();

    // ClickHandler Build
    private DeleteOrderDialogAdapterOnClickHandler mClickHandler;
    private Context context;

    public DeleteOrderDialogAdapter() {
        selectedItemPosition = 0;
    }

    public void setItemClickListener(DeleteOrderDialogAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public DeleteOrderDialogAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_delete_order_reason;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new DeleteOrderDialogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteOrderDialogAdapterViewHolder holder, int position) {
        holder.mBinding.optionName.setText(mData.get(position).getName());

        holder.mBinding.optionName.setBackgroundResource(position == selectedItemPosition ? R.color.colorPrimaryDark : R.color.gray);
        holder.mBinding.optionName.setTextColor(context.getResources().getColor(position == selectedItemPosition ? R.color.white : R.color.colorPrimaryDark));

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<CancelReason> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<CancelReason> getData() {
        return mData;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    //The interface that will be implemented later in parent activity
    public interface DeleteOrderDialogAdapterOnClickHandler {
        void onDeliverySectionItemClick(int clickedItemPosition);
    }

    public class DeleteOrderDialogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //get an Instance for dataBinding
        ListItemDeleteOrderReasonBinding mBinding;


        public DeleteOrderDialogAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemDeleteOrderReasonBinding.bind(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            selectedItemPosition = adapterPosition;
            notifyDataSetChanged();
        }

    }
}
