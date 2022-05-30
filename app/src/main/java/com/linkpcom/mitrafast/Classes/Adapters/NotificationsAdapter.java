package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Notification;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ListItemNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Notification> mData = new ArrayList<>();
    private NotificationsAdapterOnClickHandler mClickHandler;
    Context context;

    public NotificationsAdapter() {
    }

    public void setItemClickListener(NotificationsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NotificationsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapterViewHolder holder, int position) {
        Notification notification = mData.get(position);
        holder.mBinding.notification.setText(notification.getBody());
        holder.mBinding.notificationAddress.setText(notification.getTitle());
        holder.mBinding.notificationDate.setText(notification.getTime());
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<Notification> data) {
        mData = data;
        notifyDataSetChanged();
    }

    //The interface that will be implemented later in parent activity
    public interface NotificationsAdapterOnClickHandler {
        void onNotificationItemClick(Notification notification);
    }

    public class NotificationsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        ListItemNotificationBinding mBinding;

        public NotificationsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

        mBinding = ListItemNotificationBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getAdapterPosition();
            //Data passed when clicked
            Notification notification = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onNotificationItemClick(notification);
        }
    }
}