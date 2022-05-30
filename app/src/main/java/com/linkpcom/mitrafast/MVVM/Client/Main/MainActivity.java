package com.linkpcom.mitrafast.MVVM.Client.Main;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NAME;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NOTIFICATIONS;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.snackbar.Snackbar;
import com.linkpcom.mitrafast.Classes.Adapters.AdsViewPagerAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.CategoriesAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.StatusOrderAdapter;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ad;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.NotParcelable;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Notification;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AdsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Register.RegisterActivity;
import com.linkpcom.mitrafast.MVVM.Client.AboutUs.AboutUsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Cart.CartActivity;
import com.linkpcom.mitrafast.MVVM.Client.ContactUs.ContactUsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Coupons.CouponsActivity;
import com.linkpcom.mitrafast.MVVM.Client.FavoriteShops.FavoriteShopsActivity;
import com.linkpcom.mitrafast.MVVM.Client.FavoritePlaces.FavoritePlacesActivity;
import com.linkpcom.mitrafast.MVVM.Client.Locating.LocatingActivity;
import com.linkpcom.mitrafast.MVVM.Client.Notification.NotificationActivity;
import com.linkpcom.mitrafast.MVVM.Client.Offers.OffersActivity;
import com.linkpcom.mitrafast.MVVM.Client.OrderDetails.OrderDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Orders.OrdersActivity;
import com.linkpcom.mitrafast.MVVM.Client.PaymentCards.PaymentCardsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Profile.ProfileActivity;
import com.linkpcom.mitrafast.MVVM.Client.SearchShops.SearchShopsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Settings.SettingsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Shops.ShopsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Support1.Support1Activity;
import com.linkpcom.mitrafast.MVVM.Client.Wallet.WalletActivity;
import com.linkpcom.mitrafast.MVVM.Common.Login.LoginActivity;
import com.linkpcom.mitrafast.MVVM.Shop.ShopRegister.ShopRegisterActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityClientMainBinding;
import com.linkpcom.mitrafast.databinding.SideMenuViewBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends BaseActivity implements StatusOrderAdapter.OnOrderStatusListener {

    private ActivityClientMainBinding mBinding;
    public MainViewModel mViewModel;

    private final long DELAY_MS = 2000;   //delay in milliseconds before task is to be executed
    private final long PERIOD_MS = 5000;  // time in milliseconds between successive task executions.
    List<Ad> ads;
    Ad clickedAd;
    Handler handler;
    private Timer timer;
    private int currentPage = 0;
    private AdsViewPagerAdapter adsViewPagerAdapter;

    CategoriesAdapter categoriesAdapter;

    private String keyWard;
    private List<Notification> notifications;
    private StatusOrderAdapter statusOrderDt;
    private boolean listenerAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getCategoriesResponseMutableLiveData().observe(this, this::onCategoriesResponse);
        mViewModel.getAdsResponseMutableLiveData().observe(this, this::onAdsResponse);
        mViewModel.getAdClickResponseMutableLiveData().observe(this, this::onAdClickResponse);
        mViewModel.getOrderStatusMutableLiveData().observe(this, this::onOrderStatusResponse);
        mViewModel.getCurrentOrders().observe(this, this::onCurrentResponse);
        mViewModel.getNotificationsResponseMutableLiveData().observe(this, this::onNotificationsResponse);

        if (preferencesManager.getAuthenticationToken() != null) {

            //  user/currentOrders
            mViewModel.requestUserCurrentOrders();

            //  user/current-orders"
//            mViewModel.requestCurrentOrders();
        }
        statusOrderDt = new StatusOrderAdapter();
        statusOrderDt.initListener(this);
        statusOrderDt.initOrderClick(this::onOrderClicked);
        // user/currentOrders
        mViewModel.getUserCurrentOrdersResponseMutableLiveData().observe(this, this::onCurrentUserOrdersResponse);


        //New Instances
        adsViewPagerAdapter = new AdsViewPagerAdapter(this);
        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setItemClickListener(this::onCategorySelect);

        mBinding.viewPager.setAdapter(adsViewPagerAdapter);
        mBinding.services.setAdapter(categoriesAdapter);


        mBinding.ivSearch.setOnClickListener(this::onSearchClick);
        mBinding.etSearch.setOnEditorActionListener(this::onSearchKeyboardButtonClick);


        mBinding.sideMenu.contactsUs.setOnClickListener(v -> startActivity(new Intent(this, ContactUsActivity.class)));
        mBinding.sideMenu.logOut.setOnClickListener(v -> {
            preferencesManager.removeUser();
            startActivity(new Intent(this, LoginActivity.class));
        });

        SnapHelper startSnapHelper = new PagerSnapHelper();
        startSnapHelper.attachToRecyclerView(mBinding.statusOrder);

        mBinding.sideMenu.logIn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        mBinding.sideMenu.btnClose.setOnClickListener(v -> mBinding.drawerLayout.openDrawer(GravityCompat.START));
        mBinding.sideMenu.logIn.setOnClickListener(this::onLoginClick);
        mBinding.sideMenu.logOut.setOnClickListener(this::onLogoutClick);
        mBinding.sideMenu.savedLocations.setOnClickListener(this::onSavedLocationsClick);
        mBinding.sideMenu.wallet.setOnClickListener(this::onWalletClick);
        mBinding.sideMenu.cart.setOnClickListener(this::onMenuCartClick);
        mBinding.sideMenu.favorite.setOnClickListener(this::onMenuFavoriteClick);
        mBinding.sideMenu.contactsUs.setOnClickListener(this::onContactUsClick);
        mBinding.sideMenu.registerAsProvider.setOnClickListener(this::onRegisterAsProviderClick);
        mBinding.sideMenu.registerAsAgent.setOnClickListener(this::onRegisterAsAgentClick);
        mBinding.sideMenu.coupon.setOnClickListener(this::onCouponsClick);
        mBinding.sideMenu.paymentMethods.setOnClickListener(this::onPaymentMethodsClick);
        mBinding.sideMenu.settings.setOnClickListener(this::onSettingsClick);
        mBinding.services.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
                layoutParams.width = (2 * getWidth()) / 5;

                return super.checkLayoutParams(layoutParams);
            }
        });
        mBinding.bottomNavView.getMenu().findItem(R.id.nav_orders).setOnMenuItemClickListener(menuItem -> {
            if (!isLoggedIn())
                return true;
            startActivity(new Intent(this, OrdersActivity.class));
            return true;
        });
        mBinding.bottomNavView.getMenu().findItem(R.id.nav_profile).setOnMenuItemClickListener(menuItem -> {
            onProfile(null);
            return true;
        });
        mBinding.bottomNavView.getMenu().findItem(R.id.nav_offers).setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(this, OffersActivity.class));
            return true;
        });
        mBinding.bottomNavView.getMenu().findItem(R.id.nav_others).setOnMenuItemClickListener(menuItem -> {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
            return true;
        });
        mBinding.ibMenue.setOnClickListener(view -> {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
        });


        mViewModel.requestCategories();
        mViewModel.requestAds();

        initialUserName();

        mBinding.btnNotification.setOnClickListener(this::OnNotificationClicked);
    }

    private void OnNotificationClicked(View view) {
        if (preferencesManager.getAuthenticationToken() != null) {

            mViewModel.requestSeenNotification();

            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putParcelableArrayListExtra(NOTIFICATIONS, (ArrayList<Notification>) notifications);
            startActivity(intent);
        } else {
            Snackbar.make(view, getResources().getString(R.string.login_first_message), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.login), __ -> {
                        startActivity(new Intent(this, LoginActivity.class));
                    }).show();
        }
    }

    private void onOrderClicked(Order order) {
        Timber.d("os Android this is a new Click");
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra(ID, order.getId());
        startActivity(intent);
    }

    private void onOrderStatusResponse(String s) {
        //  user/currentOrders
        Timber.d("order status is : :%s", s);
        mViewModel.requestUserCurrentOrders();
        Timber.d("data is : %s", "in ");
        //  user/current-orders"
//        mViewModel.requestCurrentOrders();
    }

    private void onProfile(View o) {
        if (!isLoggedIn())
            return;
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void onNotificationsResponse(NotificationsResponse response) {
        if (response.getData().size() > 0) {
            mBinding.isNotification.setVisibility(VISIBLE);
            notifications = response.getData();
        } else {
            mBinding.isNotification.setVisibility(GONE);
        }
    }

    private void onAdsResponse(AdsResponse response) {
        if (response.isSuccess()) {

            ads = response.getData();

            //ViewPager Setup
            adsViewPagerAdapter.setData(ads);
            adsViewPagerAdapter.setItemClickListener(this::onSliderItemSelect);
//            adsViewPagerAdapter.getAdPublishSubject().subscribe(this::onSliderItemSelect);
            settingTimer();
        }
    }

    private void onSliderItemSelect(Ad clickedAd) {
        this.clickedAd = clickedAd;
        if (preferencesManager.getAuthenticationToken() != null) {
            mViewModel.requestAdClick(clickedAd.getId());
        } else {
            openLink(clickedAd);
        }
    }

    private void onAdClickResponse(BaseResponse response) {
        openLink(clickedAd);

    }

    private void openLink(Ad clickedAd) {
        if (clickedAd.getLink() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(clickedAd.getLink()));
            startActivity(intent);
        }
    }

    private void settingTimer() {
        /*After setting the adapter use the timer */

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
            Runnable Update = () -> {
                if (currentPage == adsViewPagerAdapter.getCount()) {
                    currentPage = 0;
                }
                mBinding.viewPager.setCurrentItem(currentPage++, true);
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }
    }

    private void onCurrentUserOrdersResponse(OrdersResponse response) {
        if (!response.getData().isEmpty()) {
            Intent intent = new Intent(this, LocatingActivity.class);
            intent.putExtra(CONSTANTS.INTENTS.OBJECT, response.getData().get(0));
            startActivity(intent);

        }
    }

    private void onCurrentResponse(OrdersResponse response) {
        if (!response.getData().isEmpty()) {
            statusOrderDt.setData(response.getData());
            mBinding.statusOrder.setAdapter(statusOrderDt);
//            mBinding.statusOrder.setVisibility(VISIBLE);
        }
    }

    private void OnOrderStatusClicked(Order order) {
    }

    private boolean onSearchKeyboardButtonClick(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    private void search() {
        keyWard = mBinding.etSearch.getText().toString();
        if (!keyWard.trim().equals("") || keyWard != null)
            startActivity(new Intent(this, SearchShopsActivity.class).putExtra(NAME, keyWard));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.tvLocation.setText(preferencesManager.getAddressName());
        initialUserName();

        if (preferencesManager.getAuthenticationToken() != null) {
            mViewModel.requestNotifications();
            Picasso.get().load(preferencesManager.getImage()).into(mBinding.ivProfilePic);
            mBinding.ivProfilePic.setVisibility(VISIBLE);
            mBinding.ivProfileLogo.setVisibility(GONE);
        } else {
            mBinding.ivProfilePic.setVisibility(GONE);
            mBinding.ivProfileLogo.setVisibility(VISIBLE);
        }

        if (preferencesManager.getAuthenticationToken() != null)
            mViewModel.requestUserCurrentOrders();


        if (preferencesManager.getAuthenticationToken() != null) {
            mBinding.sideMenu.headerUserName.setText(preferencesManager.getName());
            // this line can you get image user or image this app
            Picasso.get().load(preferencesManager.getImage()).placeholder(R.mipmap.main_logo).error(R.mipmap.main_logo).into(mBinding.sideMenu.headerUserImage);

            mBinding.sideMenu.logIn.setVisibility(View.GONE);
            mBinding.sideMenu.registerAsProvider.setVisibility(View.GONE);
            mBinding.sideMenu.registerAsAgent.setVisibility(View.GONE);
            mBinding.sideMenu.logOut.setVisibility(View.VISIBLE);
            mBinding.sideMenu.coupon.setVisibility(View.VISIBLE);
            mBinding.sideMenu.paymentMethods.setVisibility(View.VISIBLE);
            mBinding.sideMenu.wallet.setVisibility(View.VISIBLE);

        } else {
            mBinding.sideMenu.headerUserName.setText(getString(R.string.app_name));
            mBinding.sideMenu.headerUserImage.setImageResource(R.mipmap.main_logo);

            mBinding.sideMenu.logIn.setVisibility(View.VISIBLE);
            mBinding.sideMenu.registerAsProvider.setVisibility(View.VISIBLE);
            mBinding.sideMenu.registerAsAgent.setVisibility(View.VISIBLE);

            mBinding.sideMenu.logOut.setVisibility(View.GONE);
            mBinding.sideMenu.coupon.setVisibility(View.GONE);
            mBinding.sideMenu.paymentMethods.setVisibility(View.GONE);
            mBinding.sideMenu.wallet.setVisibility(View.GONE);
        }
    }

    private void onCategorySelect(Category category) {
        Intent intent = new Intent(this, ShopsActivity.class);
        intent.putExtra(ID, category.getId());
        intent.putExtra(NAME, category.getName());

        startActivity(intent);
    }

    private void onCategoriesResponse(CategoriesResponse response) {
        categoriesAdapter.setData(response.getData());

    }

    private void onSearchClick(View view) {
        search();
    }

    private void onSavedLocationsClick(View view) {
        if (!isLoggedIn())
            return;
        startActivity(new Intent(this, FavoritePlacesActivity.class));
    }

    private void onSettingsClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));

    }

    private void onLoginClick(View view) {

        startActivity(new Intent(this, LoginActivity.class));

    }

    private void onLogoutClick(View view) {
        mBinding.drawerLayout.close();
        preferencesManager.removeUser();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void onWalletClick(View v) {
        if (!isLoggedIn())
            return;
        startActivity(new Intent(this, WalletActivity.class));
    }

    private void onCouponsClick(View v) {
        if (!isLoggedIn())
            return;
        startActivity(new Intent(this, CouponsActivity.class));
    }

    private void onPaymentMethodsClick(View v) {
        if (!isLoggedIn())
            return;
        startActivity(new Intent(this, PaymentCardsActivity.class));
    }

    private void onMenuCartClick(View v) {

        startActivity(new Intent(this, CartActivity.class));
    }

    private void onMenuFavoriteClick(View v) {
        if (isLoggedIn())
            startActivity(new Intent(this, FavoriteShopsActivity.class));
    }

    private void onAboutClick(View v) {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    private void onContactUsClick(View v) {
        startActivity(new Intent(this, Support1Activity.class));

    }

    private void onRegisterAsProviderClick(View v) {
        startActivity(new Intent(this, ShopRegisterActivity.class));
    }

    private void onRegisterAsAgentClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle(R.string.exit_from_mitra_fast);
        builder.setMessage(R.string.do_you_want_to_exit);
        builder.setPositiveButton(R.string.exit_btn, (dialog, id) -> finish());
        builder.setNegativeButton(R.string.discard_btn, (dialog, id) -> dialog.cancel());
        builder.show();
        SideMenuViewBinding sideMenu = mBinding.sideMenu;
    }

    public void initialUserName() {
        if (preferencesManager.getAuthenticationToken() != null)
            mBinding.tvName.setText(String.format("%s %s", getString(R.string.hello), preferencesManager.getName()));
        else
            mBinding.tvName.setText(getString(R.string.hello));
    }

    @Override
    public void onOrderStatusChanged(int id) {
        Timber.d("status changed on order #%s", id);
//        mViewModel.requestCurrentOrders();
    }
}