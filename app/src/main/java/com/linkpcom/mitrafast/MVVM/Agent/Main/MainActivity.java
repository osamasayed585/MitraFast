package com.linkpcom.mitrafast.MVVM.Agent.Main;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.Adapters.NewOrdersAdapter;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Agent;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentProfileResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.BankAccount.BankAccountActivity;
import com.linkpcom.mitrafast.MVVM.Agent.ContactUs.ContactUsActivity;
import com.linkpcom.mitrafast.MVVM.Agent.FinancialMovements.FinancialMovementsActivity;
import com.linkpcom.mitrafast.MVVM.Agent.FinishedOrders.FinishedOrdersActivity;
import com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder.InProgressOrderActivity;
import com.linkpcom.mitrafast.MVVM.Agent.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Profile.ProfileActivity;
import com.linkpcom.mitrafast.MVVM.Agent.TermsAndConditions.TermsAndConditionsActivity;
import com.linkpcom.mitrafast.MVVM.Agent.notifications.AgentNotificationsActivity;
import com.linkpcom.mitrafast.MVVM.Client.AboutUs.AboutUsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Wallet.WalletActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAgentMainBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    private ActivityAgentMainBinding mBinding;
    public MainViewModel mViewModel;
    private Agent user;
    private NewOrdersAdapter ordersAdapter;
    private LatLng currentLocation;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mCompositeDisposable = new CompositeDisposable();
        if (errorHandlerDialog.mStopMail != null)
            mCompositeDisposable.add(errorHandlerDialog.mStopMail.subscribe(this::onStopService));

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getProfileResponseMutableLiveData().observe(this, this::onProfileResponse);
        mViewModel.getCurrentLocationResponseMutableLiveData().observe(this, this::onCurrentLocationResponse);
        mViewModel.getCurrentOrdersResponseMutableLiveData().observe(this, this::onCurrentOrdersResponse);

        mViewModel.getOrderAddedMutableLiveData().observe(this, this::onOrderAddedResponse);
        mViewModel.getOrderRemovedMutableLiveData().observe(this, this::onOrderRemovedResponse);
        mViewModel.getOrderMiniDetailsResponseMutableLiveData().observe(this, this::onOrderMiniDetailsResponse);
        mViewModel.getCurrentLocationResponseMutableLiveData().observe(this, this::onCurrentLocationResponse);
        mViewModel.requestWalletBalance().observe(this, this::OnWalletBalanceResponse);
        mBinding.wallet.setOnClickListener(v -> startActivity(new Intent(this, WalletActivity.class)));
        //New Instances
        ordersAdapter = new NewOrdersAdapter();

        ordersAdapter.setItemClickListener(this::onOrderDetailsClick);

        mBinding.rvOffers.setAdapter(ordersAdapter);

        mViewModel.initLocationUtils(this);
        //New Instances

        mBinding.icNotification.setOnClickListener(v -> {
            startActivity(new Intent(this, AgentNotificationsActivity.class));
        });
        mBinding.imageButton.setOnClickListener(v1 -> mBinding.drawerLayout.openDrawer(GravityCompat.START));
        mBinding.home.setOnClickListener(v -> mBinding.drawerLayout.close());
        mBinding.profile.setOnClickListener(this::onProfileClick);
        mBinding.finishedOrders.setOnClickListener(this::onFinishedOrdersClick);
        mBinding.financialMovement.setOnClickListener(this::onFinancialMovementClick);
        mBinding.bankAccount.setOnClickListener(this::onBankAccountClick);
        mBinding.termsAndConditions.setOnClickListener(this::onTermsAndConditionsClick);
        mBinding.contactsUs.setOnClickListener(this::onContactUsClick);
        mBinding.aboutUs.setOnClickListener(this::onAboutUsClick);
        mBinding.logOut.setOnClickListener(this::onLogoutClick);

        mBinding.deliveryService.setChecked(preferencesManager.getDeliveryService());

        mBinding.deliveryService.setOnCheckedChangeListener((compoundButton, isOpen) -> {
            startService(isOpen);
            preferencesManager.setDeliveryService(isOpen);
            if (isOpen) {
                mViewModel.startOrdersListening(String.valueOf(user.getId()));
            } else {
                ordersAdapter.clear();
                mViewModel.removeOrdersListener();
            }
        });

        mBinding.tvCompletedOrders.setOnClickListener(this::onFinishedOrdersClick);
    }

    private void onStopService(Boolean aBoolean) {
        preferencesManager.setAvailable(aBoolean);
        mViewModel.getLocationUtils().stopService();
        preferencesManager.setDeliveryService(aBoolean);

        ordersAdapter.clear();
        mViewModel.removeOrdersListener();

    }

    private void onAboutUsClick(View view) {

        startActivity(new Intent(this, AboutUsActivity.class));
    }

    private void OnWalletBalanceResponse(WalletResponse walletResponse) {
        mBinding.walletBalance.setText(String.format("%s %s", walletResponse.getData().getValue(), walletResponse.getData().getCurrency()));
    }

    private void startService(boolean isStart) {
        preferencesManager.setAvailable(isStart);

        if (preferencesManager.isAvailable()) {
            mViewModel.getLocationUtils().requestPermissions();
            mViewModel.requestRemoveOfflineLocation();
        } else {
            mViewModel.getLocationUtils().stopService();
            mViewModel.requestRemoveAvailableLocation();
            //Get Current Location To Add Offline Location
            mViewModel.requestCurrentLocation(this);
        }
    }

    private void onCurrentOrdersResponse(OrdersResponse response) {
        if (!response.getData().isEmpty()) {
            if (response.getData().get(0).getOrder_status().getId() > 2) {
                preferencesManager.setBusy(true);
                mViewModel.requestRemoveAvailableLocation();
                Intent intent = new Intent(this, InProgressOrderActivity.class);
                intent.putExtra(CONSTANTS.INTENTS.ID, response.getData().get(0).getId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(CONSTANTS.INTENTS.ID, response.getData().get(0).getId());
                intent.putExtra(CONSTANTS.INTENTS.IS_WAITING, true);
                startActivity(intent);
            }
        } else {
            preferencesManager.setBusy(false);
            mViewModel.requestRemoveBusyLocation();

        }

    }

    private void onProfileResponse(AgentProfileResponse response) {
        user = response.getData();
        Picasso.get().load(response.getData().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivProfilePic);
        mBinding.tvName.setText(response.getData().getName());
        mBinding.headerUserName.setText(response.getData().getName());
        mBinding.tvMembership.setText(String.format("%s: #%s", getString(R.string.membership_number), response.getData().getMembership_number()));

        mBinding.tvCompletedOrders.setText(String.format("%s : %s", getString(R.string.completed_orders), response.getData().getFinish_orders_count()));
        mBinding.tvCanceledOrder.setText(String.format("%s : %s", getString(R.string.canceled_orders), response.getData().getCancel_orders_count()));

        boolean isApproved = user.is_profile_approved();


        if (!isApproved) {
            startService(false);
            mBinding.isProfileApproved.setVisibility(VISIBLE);
        } else {
            mBinding.isProfileApproved.setVisibility(GONE);
            startService(preferencesManager.isAvailable());
            mViewModel.startOrdersListening(String.valueOf(user.getId()));
        }


    }

    private void onCurrentLocationResponse(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (!preferencesManager.isAvailable())
            mViewModel.requestAddOfflineLocation(location);
        else {
            mViewModel.requestRemoveAvailableLocation();
        }

        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        ordersAdapter.setCurrentLocation(currentLocation);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestProfile();
        mViewModel.getLocationUtils().onStart();
        mViewModel.requestAgentCurrentOrders();
        if (preferencesManager.getAuthenticationToken() != null) {
            mBinding.headerUserName.setText(preferencesManager.getName());
            // this line can you get image user or image this app
            Picasso.get().load(preferencesManager.getImage()).placeholder(R.mipmap.main_logo).error(R.mipmap.main_logo).into(mBinding.headerUserImage);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.requestCurrentLocation(this);
    }

    @Override
    protected void onStop() {
        mViewModel.getLocationUtils().onStop();
        super.onStop();
    }

    private void onProfileClick(View v) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void onFinishedOrdersClick(View v) {
        startActivity(new Intent(this, FinishedOrdersActivity.class));
    }

    private void onFinancialMovementClick(View view) {
        startActivity(new Intent(this, FinancialMovementsActivity.class));

    }

    private void onBankAccountClick(View v) {
        startActivity(new Intent(this, BankAccountActivity.class));
    }

    private void onTermsAndConditionsClick(View v) {
        startActivity(new Intent(this, TermsAndConditionsActivity.class));
    }

    private void onContactUsClick(View v) {
        startActivity(new Intent(this, ContactUsActivity.class));
    }

    private void onLogoutClick(View view) {
        mBinding.drawerLayout.close();
        preferencesManager.removeUser();
        mViewModel.getLocationUtils().stopService();
        Intent intent = new Intent(this, com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onOrderAddedResponse(String orderId) {
        mViewModel.requestOrderDetails(Integer.parseInt(orderId));
    }

    private void onOrderRemovedResponse(String orderId) {
        ordersAdapter.removeOrder(orderId);
    }

    private void onOrderMiniDetailsResponse(OrderDetailsResponse response) {
        ordersAdapter.addOrder(response.getData());
    }

    public void onOrderDetailsClick(Order order) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(CONSTANTS.INTENTS.ID, order.getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle(R.string.Attention);
        builder.setMessage(R.string.do_you_want_to_exit_for_agent);
        builder.setPositiveButton(R.string.exit_btn, (dialog, id) -> finish());
        builder.setNegativeButton(R.string.discard_btn, (dialog, id) -> dialog.cancel());
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

}