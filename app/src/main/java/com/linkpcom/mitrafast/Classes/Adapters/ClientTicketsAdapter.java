package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Reply;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.TicketStatus;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemClientTicketBinding;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class ClientTicketsAdapter extends RecyclerView.Adapter<ClientTicketsAdapter.TicketsAdapterViewHolder> {
    private List<Ticket> mData;
    private List<Reply> mReply;
    private Context context;
    private ClientReplyAdapter clientReplyAdapter;
    private TicketsAdapterOnClickHandler mClickHandler;
    public PublishSubject<Integer> itemClickStream = PublishSubject.create();

    public ClientTicketsAdapter() {
    }

    public void setItemClickListener(TicketsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public TicketsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_client_ticket, parent, false);
        return new TicketsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapterViewHolder holder, int position) {
        holder.mBinding.tvSubject.setText(String.format("%s %s", context.getText(R.string.type_ticket), mData.get(position).getSupport_section().getName()));
        holder.mBinding.tvMessage.setText(mData.get(position).getMessage());
        holder.mBinding.tvRequestNumber.setText(String.format("#%s %s", context.getString(R.string.request_number), mData.get(position).getId()));
        holder.mBinding.tvRequestDate.setText(String.format("%s %s", context.getText(R.string.request_date), mData.get(position).getTime()));
        TicketStatus ticketStatuses = mData.get(position).getSupport_ticket_statuses();
        if (context.getResources().getConfiguration().locale.getLanguage().equals("en"))
            holder.mBinding.tvRequestStatus.setText(Html.fromHtml( String.format("%s %s", context.getText(R.string.request_status),getColoredSpanned(ticketStatuses.getName_en(),ticketStatuses.getStatus_color()))));
        else
            holder.mBinding.tvRequestStatus.setText(Html.fromHtml(String.format("%s %s", context.getText(R.string.request_status), getColoredSpanned(ticketStatuses.getName_ar(),ticketStatuses.getStatus_color()))));

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
        //get an Instance for dataBinding
        ListItemClientTicketBinding mBinding;

        public TicketsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = ListItemClientTicketBinding.bind(itemView);
            mBinding.tvAddReply.setOnClickListener(this::onAddReplyClick);
            mBinding.llHeaderContainer.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            itemClickStream.onNext(mData.get(getBindingAdapterPosition()).getId());
            mData.get(getBindingAdapterPosition()).inverseVisibility();
            notifyItemChanged(getBindingAdapterPosition());
        }

        public void onAddReplyClick(View view) {
            if (mClickHandler != null)
                mClickHandler.onAddReplyClick(mData.get(getBindingAdapterPosition()));
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

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}