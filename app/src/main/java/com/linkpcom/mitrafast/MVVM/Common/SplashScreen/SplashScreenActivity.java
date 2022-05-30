package com.linkpcom.mitrafast.MVVM.Common.SplashScreen;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder.InProgressOrderActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity;

import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Common.Intro.IntroActivity;
import com.linkpcom.mitrafast.databinding.ActivitySplashScreenBinding;

import timber.log.Timber;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashScreenActivity extends BaseActivity {

    public static final String is_user = "3";
    ActivitySplashScreenBinding mBinding;
    SplashScreenViewModel mViewModel;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getDeleteCartItemsMutableLiveData().observe(this, this::onDeleteCartItemsResponse);

        mViewModel.requestDeleteCartItems();
    }

    private void onDeleteCartItemsResponse(Boolean aBoolean) {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::validation, 1000);
    }

    private void validation() {
        //IF He Didn't Verify His Account
        if (!preferencesManager.isUserVerified() || !preferencesManager.isProfileComplete() || preferencesManager.isResetPassword())
            preferencesManager.removeUser();


        notificationsHandler();


    }

    // NEWORDER  = '1';
    private void notificationsHandler() {
        Intent mainIntent = new Intent(this, IntroActivity.class);
        Bundle extras = getIntent().getExtras();

        if (preferencesManager.getAuthenticationToken() == null) {
            finish();
            startActivity(mainIntent);
            return;
        }

        if (extras != null) {

            String order_id = extras.getString("item_id");
            String type_id = extras.getString("type_id");

            if (preferencesManager.getUserTypeId().equals(is_user)) {

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                taskStackBuilder.addNextIntentWithParentStack(mainIntent);

                Intent intent;
                if (order_id != null) {
                    intent = new Intent(this, InProgressOrderActivity.class);
                    intent.putExtra(CONSTANTS.INTENTS.ID, Integer.parseInt(order_id));
                } else {
                    intent = new Intent(this, MainActivity.class);
                }
                taskStackBuilder.addNextIntent(intent);
                finish();
                taskStackBuilder.startActivities();
            } else {
                if (order_id != null && type_id.equals("1")) {
                    Log.d("====", "notificationsHandler: one");
                    Intent intent = new Intent(this, OrderDetailsActivity.class);
                    intent.putExtra(CONSTANTS.INTENTS.ID, Integer.parseInt(order_id));
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("====", "notificationsHandler: Called");
                    finish();
                    startActivity(mainIntent);
                }
            }
        } else {
            finish();
            startActivity(mainIntent);
        }
    }
}