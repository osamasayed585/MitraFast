package com.linkpcom.mitrafast.Classes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Notification;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.RowItemNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class ClientNotificationsAdapter extends RecyclerView.Adapter<ClientNotificationsAdapter.NotificationsAdapterViewHolder> {
    //The list of Objects that will populate the RecyclerView
    private List<Notification> mData = new ArrayList<>();
    private NotificationsAdapterOnClickHandler mClickHandler;
    Context context;

    public ClientNotificationsAdapter() {
    }

    public void setItemClickListener(NotificationsAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NotificationsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_notification, parent, false);
        return new NotificationsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapterViewHolder holder, int position) {
        Notification notification = mData.get(position);
        holder.bind(notification);
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

    public class NotificationsAdapterViewHolder extends RecyclerView.ViewHolder {
        //get an Instance for dataBinding
        RowItemNotificationBinding mBinding;

        public NotificationsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mBinding = RowItemNotificationBinding.bind(itemView);
            itemView.setOnClickListener(this::onItemClick);
        }

        public void onItemClick(View view) {
            int adapterPosition = getBindingAdapterPosition();
            //Data passed when clicked
            Notification notification = mData.get(adapterPosition);
            if (mClickHandler != null)
                mClickHandler.onNotificationItemClick(notification);
        }

        private void bind(Notification notification) {
            mBinding.title.setText(notification.getTitle());
            mBinding.body.setText(notification.getBody());
            mBinding.time.setText(notification.getTime());

            if (notification.isViewed()) {
                mBinding.isViewed.setBackground(context.getResources().getDrawable(R.drawable.rounded_corners_solid));
                mBinding.title.setTextColor(context.getResources().getColor(R.color.black));
                mBinding.body.setTextColor(context.getResources().getColor(R.color.black));
            }

        }
    }

    //The interface that will be implemented later in parent activity
    public interface NotificationsAdapterOnClickHandler {
        void onNotificationItemClick(Notification notification);
    }
}