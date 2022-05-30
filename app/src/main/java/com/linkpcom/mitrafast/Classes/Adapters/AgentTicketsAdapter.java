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
import com.linkpcom.mitrafast.databinding.ListItemAgentTicketBinding;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;

public class AgentTicketsAdapter extends RecyclerView.Adapter<AgentTicketsAdapter.TicketsAdapterViewHolder> {

    private List<Ticket> mData = new ArrayList<>();
    private ClientReplyAdapter clientReplyAdapter;
    private TicketsAdapterOnClickHandler mClickHandler;
    public PublishSubject<Integer> itemClickStream = PublishSubject.create();
    private List<Reply> mReply;
    private Context context;

    public AgentTicketsAdapter() {
    }

    public void setItemClickListener(TicketsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TicketsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_agent_ticket, parent, false);
        return new TicketsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapterViewHolder holder, int position) {
        Ticket ticket = mData.get(position);
        holder.mBinding.tvTicketNo.setText(String.valueOf(ticket.getId()));
        holder.mBinding.tvTicketType.setText(ticket.getTicket_type().getName());
        holder.mBinding.ticketState.setText(ticket.getTicket_status_name());

        holder.mBinding.tvMessage.setText(ticket.getMessage());

        if (mReply != null)
            holder.populateReplies(mReply);

        holder.mBinding.ivArrow.setImageResource(mData.get(position).is_visible() ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
        holder.mBinding.cvMessageContainer.setVisibility(mData.get(position).is_visible() ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Ticket> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setReply(List<Reply> reply) {
        mReply = reply;
        notifyDataSetChanged();
    }

    public class TicketsAdapterViewHolder extends RecyclerView.ViewHolder {
        ListItemAgentTicketBinding mBinding;

        public TicketsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemAgentTicketBinding.bind(itemView);
            mBinding.tvAddReply.setOnClickListener(this::onAddReplyClick);
            mBinding.TicketLayout.setOnClickListener(this::onItemClick);
        }

        public void onAddReplyClick(View view) {
            if (mClickHandler != null)
                mClickHandler.onAddReplyClick(mData.get(getBindingAdapterPosition()));
        }

        public void onItemClick(View view) {
            itemClickStream.onNext(mData.get(getBindingAdapterPosition()).getId());
            mData.get(getBindingAdapterPosition()).inverseVisibility();
            notifyItemChanged(getBindingAdapterPosition());
        }

        public void populateReplies(List<Reply> replies) {
            clientReplyAdapter = new ClientReplyAdapter();
            clientReplyAdapter.setData(replies);
            mBinding.Replies.setAdapter(clientReplyAdapter);
        }
    }

    public interface TicketsAdapterOnClickHandler {
        void onAddReplyClick(Ticket ticket);
    }
}