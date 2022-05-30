package com.linkpcom.mitrafast.MVVM.Client.Notification;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NOTIFICATIONS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.linkpcom.mitrafast.Classes.Adapters.ClientNotificationsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Notification;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityNotificationBinding;

import java.util.List;

public class NotificationActivity extends BaseActivity {

    private List<Notification> notifications;
    private ClientNotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNotificationBinding mBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        notificationsAdapter = new ClientNotificationsAdapter();
        notifications = getIntent().getParcelableArrayListExtra(NOTIFICATIONS);

        notificationsAdapter.setData(notifications);
        mBinding.RvNotifications.setAdapter(notificationsAdapter);

        notificationsAdapter.setItemClickListener(this::onNotificationClicked);
    }

    private void onNotificationClicked(Notification notification) {
        Log.d("====", "onNotificationClicked: " + notification.getId());
        startActivity(new Intent(this, OrderDetailsActivity.class).putExtra(ID, notification.getOrder_id()));
    }
}