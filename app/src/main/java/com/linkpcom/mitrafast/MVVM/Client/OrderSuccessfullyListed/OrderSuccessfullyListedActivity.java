package com.linkpcom.mitrafast.MVVM.Client.OrderSuccessfullyListed;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;



import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Orders.OrdersActivity;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.databinding.ActivityOrderSuccessfullyListedBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class OrderSuccessfullyListedActivity extends BaseActivity {
    ActivityOrderSuccessfullyListedBinding mBinding;

    String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOrderSuccessfullyListedBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getStringExtra(ID);

        mBinding.orderId.setText(String.valueOf(orderId));

        mBinding.home.setOnClickListener(this::onHomeClick);
        mBinding.orderStatus.setOnClickListener(this::onOrderStatusClick);

    }

    private void onOrderStatusClick(View view) {
        initTaskStackBuilder();
    }

    private void initTaskStackBuilder() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);


        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        taskStackBuilder.addNextIntentWithParentStack(mainIntent);

        Intent ordersIntent = new Intent(this, OrdersActivity.class);
        taskStackBuilder.addNextIntentWithParentStack(ordersIntent);

        Intent orderDetailsIntent = new Intent(this, OrderDetailsActivity.class);
        orderDetailsIntent.putExtra(ID, Integer.parseInt(orderId));
        taskStackBuilder.addNextIntentWithParentStack(orderDetailsIntent);

        taskStackBuilder.startActivities();
    }

    @Override
    public void onBackPressed() {
        initTaskStackBuilder();
    }

    @Override
    public void onBackClick(View view) {
        initTaskStackBuilder();
    }

    @Override
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}