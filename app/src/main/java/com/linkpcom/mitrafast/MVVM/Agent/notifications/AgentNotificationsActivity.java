package com.linkpcom.mitrafast.MVVM.Agent.notifications;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.NotificationsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivityAgentNotificationsBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AgentNotificationsActivity extends BaseActivity {

    private ActivityAgentNotificationsBinding mBinding;
    private NotificationsAdapter notificationsAdapter;
    private AgentNotificationsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentNotificationsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(AgentNotificationsViewModel.class);
        mViewModel.getNotificationsResponseMutableLiveData().observe(this, this::onNotificationsResponse);


        notificationsAdapter = new NotificationsAdapter();
        mBinding.notifications.setAdapter(notificationsAdapter);
    }


    private void onNotificationsResponse(NotificationsResponse response) {
        notificationsAdapter.setData(response.getData());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestNotifications();
    }
}