package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemOptionBinding;

import java.util.ArrayList;
import java.util.List;


public class OptionsDialogAdapter extends RecyclerView.Adapter<OptionsDialogAdapter.OptionsDialogAdapterViewHolder> {

    //The list of Objects that will populate the RecyclerView
    private List<String> mData = new ArrayList<>();

    // ClickHandler Build
    private OptionsDialogAdapterOnClickHandler mClickHandler;

    public OptionsDialogAdapter() {
    }

    public void setItemClickListener(OptionsDialogAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public OptionsDialogAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_option;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new OptionsDialogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsDialogAdapterViewHolder holder, int position) {
        holder.mBinding.optionName.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<String> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface OptionsDialogAdapterOnClickHandler {
        void onDeliverySectionItemClick(int clickedItemPosition);
    }

    public class OptionsDialogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //get an Instance for dataBinding
        ListItemOptionBinding mBinding;


        public OptionsDialogAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemOptionBinding.bind(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            if (mClickHandler != null)
                mClickHandler.onDeliverySectionItemClick(adapterPosition);
        }

    }

}
