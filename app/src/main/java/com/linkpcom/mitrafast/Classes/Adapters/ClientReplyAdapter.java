package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Reply;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemClientTicketBinding;
import com.linkpcom.mitrafast.databinding.RowReplyMassageBinding;

import java.util.List;

import rx.subjects.PublishSubject;

public class ClientReplyAdapter extends RecyclerView.Adapter<ClientReplyAdapter.TicketsAdapterViewHolder> {

    private List<Reply> mData;
    public PublishSubject<Integer> itemClickStream = PublishSubject.create();
    private ClientReplyImagesAdapter clientReplyImagesAdapter;

    @NonNull
    @Override
    public TicketsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reply_massage, parent, false);
        return new TicketsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapterViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Reply> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class TicketsAdapterViewHolder extends RecyclerView.ViewHolder {
        RowReplyMassageBinding mBinding;

        public TicketsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = RowReplyMassageBinding.bind(itemView);
        }

        public void bind(Reply reply) {
            mBinding.tvReply.setText(reply.getReply());
            mBinding.tvName.setText(reply.getUser().getName());
            mBinding.tvDate.setText(reply.getCreated_at());
            if (reply.getSupport_ticket_reply_image() != null) {
                clientReplyImagesAdapter = new ClientReplyImagesAdapter();
                clientReplyImagesAdapter.setData(reply.getSupport_ticket_reply_image());
                mBinding.replyImages.setAdapter(clientReplyImagesAdapter);
            }
        }
    }
}