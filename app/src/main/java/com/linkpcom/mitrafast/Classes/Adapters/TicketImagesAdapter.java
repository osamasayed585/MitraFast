package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemTicketImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TicketImagesAdapter extends RecyclerView.Adapter<TicketImagesAdapter.TicketImagesAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Image> mData = new ArrayList<>();
    private TicketImagesAdapterOnClickHandler mClickHandler;
    Context context;

    public TicketImagesAdapter() {
    }

    public void setItemClickListener(TicketImagesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TicketImagesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ticket_image, parent, false);
        return new TicketImagesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketImagesAdapterViewHolder holder, int position) {
        Image ticketImage = mData.get(position);

        File imgFile = new File(ticketImage.getPath());
        if (imgFile.exists())
            Glide.with(context)
                    .load(imgFile)
                    .placeholder(R.drawable.logo_holder)
                    .error(R.drawable.logo_holder)
                    .override(100, 200)
                    .into(holder.mBinding.image);

        holder.mBinding.btnAddPics.setVisibility(position == mData.size() - 1 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Image> data) {
        mData = data;
        notifyDataSetChanged();
    }


    public List<Image> getData() {
        return mData;
    }

    //The interface that will be implemented later in parent activity
    public interface TicketImagesAdapterOnClickHandler {
        void onAddMoreImagesClick(View view);
    }

    public class TicketImagesAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemTicketImageBinding mBinding;

        public TicketImagesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemTicketImageBinding.bind(itemView);
            mBinding.btnAddPics.setOnClickListener(this::onAddMoreImagesClick);
        }

        private void onAddMoreImagesClick(View view) {
            if (mClickHandler != null)
                mClickHandler.onAddMoreImagesClick(view);
        }
    }
}