package com.linkpcom.mitrafast.Classes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Reply;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.ReplyImages;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.RowReplyImageBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.subjects.PublishSubject;

public class ClientReplyImagesAdapter extends RecyclerView.Adapter<ClientReplyImagesAdapter.TicketsAdapterViewHolder> {

    private List<ReplyImages> mData;
    public PublishSubject<String> onClickImage = PublishSubject.create();

    @NonNull
    @Override
    public TicketsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reply_image, parent, false);
        return new TicketsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapterViewHolder holder, int position) {
        Picasso.get().load(mData.get(position).getImage()).into(holder.mBinding.replyImage);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<ReplyImages> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class TicketsAdapterViewHolder extends RecyclerView.ViewHolder {
        RowReplyImageBinding mBinding;

        public TicketsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = RowReplyImageBinding.bind(itemView);
            mBinding.replyImage.setOnClickListener(this::onImageClicked);
        }

        private void onImageClicked(View view) {
            onClickImage.onNext(mData.get(getBindingAdapterPosition()).getImage());
        }
    }
}