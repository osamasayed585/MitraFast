package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.InfoString;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemAboutUsPointBinding;

import java.util.ArrayList;
import java.util.List;

public class AboutUsListsAdapter extends RecyclerView.Adapter<AboutUsListsAdapter.AboutUsListsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<InfoString> mData = new ArrayList<>();
    private AboutUsListsAdapterOnClickHandler mClickHandler;
    Context context;

    public AboutUsListsAdapter() {
    }

    public void setItemClickListener(AboutUsListsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AboutUsListsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_about_us_point, parent, false);
        return new AboutUsListsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutUsListsAdapterViewHolder holder, int position) {
        InfoString aboutUsPoint = mData.get(position);
        holder.mBinding.question.setText(aboutUsPoint.getName());
        holder.mBinding.answer.setText(aboutUsPoint.getText());
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<InfoString> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface AboutUsListsAdapterOnClickHandler {
        void onAboutUsPointItemClick(InfoString aboutUsPoint);
    }

    public class AboutUsListsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemAboutUsPointBinding mBinding;

        public AboutUsListsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ListItemAboutUsPointBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            InfoString aboutUsPoint = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onAboutUsPointItemClick(aboutUsPoint);
        }
    }
}