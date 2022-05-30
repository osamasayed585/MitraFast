package com.linkpcom.mitrafast.Classes.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.github.florent37.shapeofview.shapes.DiagonalView;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.Agent.Register.RegisterActivity;
import com.linkpcom.mitrafast.MVVM.Client.AboutUs.AboutUsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Cart.CartActivity;
import com.linkpcom.mitrafast.MVVM.Client.Coupons.CouponsActivity;
import com.linkpcom.mitrafast.MVVM.Client.FavoritePlaces.FavoritePlacesActivity;
import com.linkpcom.mitrafast.MVVM.Client.PaymentCards.PaymentCardsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Profile.ProfileActivity;
import com.linkpcom.mitrafast.MVVM.Client.Settings.SettingsActivity;
import com.linkpcom.mitrafast.MVVM.Client.Support1.Support1Activity;
import com.linkpcom.mitrafast.MVVM.Client.Wallet.WalletActivity;
import com.linkpcom.mitrafast.MVVM.Common.FAQ.FAQActivity;
import com.linkpcom.mitrafast.MVVM.Common.Login.LoginActivity;
import com.linkpcom.mitrafast.MVVM.Shop.ShopRegister.ShopRegisterActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.DialogNavigationViewBinding;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.PICK_PROFILE_IMAGE_FOR_NAV;


public class NavigationViewDialog {


    DialogNavigationViewBinding mBinding;
    PreferencesManager preferencesManager;
    Activity activity;
    private Dialog mDialog;
    ConfirmDialog loginDialog;

    public NavigationViewDialog(Activity activity) {
        this.activity = activity;
        mDialog = new Dialog(activity, android.R.style.Theme_Light);
        preferencesManager = new PreferencesManager(activity);
        loginDialog = new ConfirmDialog(activity);

        loginDialog.setTitle(activity.getString(R.string.login_first_message));
        loginDialog.setConfirmListDialogHandler(() -> activity.startActivity(new Intent(activity, LoginActivity.class)));

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DialogNavigationViewBinding.inflate(LayoutInflater.from(activity));
        mDialog.setContentView(mBinding.getRoot());
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 0);
        mDialog.getWindow().setBackgroundDrawable(inset);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        mBinding.btnClose.setOnClickListener(v -> dismiss());
        mBinding.navLogin.setOnClickListener(this::onLoginClick);
        mBinding.navLogOut.setOnClickListener(this::onLogoutClick);
        mBinding.navProfile.setOnClickListener(this::onProfileClick);
        mBinding.navSavedLocations.setOnClickListener(this::onSavedLocationsClick);
        mBinding.navWallet.setOnClickListener(this::onWalletClick);
        mBinding.navCart.setOnClickListener(this::onMenuCartClick);
        mBinding.navAboutUs.setOnClickListener(this::onAboutClick);
        mBinding.navContactsUs.setOnClickListener(this::onContactUsClick);
        mBinding.navRegisterAsProvider.setOnClickListener(this::onRegisterAsProviderClick);
        mBinding.navRegisterAsAgent.setOnClickListener(this::onRegisterAsAgentClick);
        mBinding.navFaq.setOnClickListener(this::onFAQClick);
        mBinding.navCoupons.setOnClickListener(this::onCouponsClick);
        mBinding.navPaymentMethods.setOnClickListener(this::onPaymentMethodsClick);
        mBinding.navSettings.setOnClickListener(this::onSettingsClick);
        mBinding.headerUserImage.setOnClickListener(this::onProfileImageClick);
    }



    private void onProfileClick(View v) {
        if (!isLoggedIn())
            return;
        activity.startActivity(new Intent(activity, ProfileActivity.class));
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PROFILE_IMAGE_FOR_NAV && data != null) {
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            File file = new File(image.getPath());
            if (file.exists()) {
                if (mClickHandler != null)
                    mClickHandler.onImageSelect(image.getPath());
            }
        }
    }

    public void setImage(String image) {
        preferencesManager.setImage(image);
        Picasso.get().load(preferencesManager.getImage()).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(mBinding.headerUserImage);
    }


    public void onStart() {

        if (preferencesManager.getAuthenticationToken() != null) {
            mBinding.headerUserName.setText(preferencesManager.getName());
            Picasso.get().load(preferencesManager.getImage()).placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(mBinding.headerUserImage);

            mBinding.navLogin.setVisibility(View.GONE);
            mBinding.navRegisterAsProvider.setVisibility(View.GONE);
            mBinding.navRegisterAsAgent.setVisibility(View.GONE);

            mBinding.navLogOut.setVisibility(View.VISIBLE);
            mBinding.navProfile.setVisibility(View.VISIBLE);
            mBinding.navCoupons.setVisibility(View.VISIBLE);
            mBinding.navPaymentMethods.setVisibility(View.VISIBLE);
            mBinding.navWallet.setVisibility(View.VISIBLE);

        } else {
            mBinding.headerUserName.setText(activity.getString(R.string.app_name));
            mBinding.headerUserImage.setImageResource(R.drawable.ic_profile);

            mBinding.navLogin.setVisibility(View.VISIBLE);
            mBinding.navRegisterAsProvider.setVisibility(View.VISIBLE);
            mBinding.navRegisterAsAgent.setVisibility(View.VISIBLE);

            mBinding.navLogOut.setVisibility(View.GONE);
            mBinding.navProfile.setVisibility(View.GONE);
            mBinding.navCoupons.setVisibility(View.GONE);
            mBinding.navPaymentMethods.setVisibility(View.GONE);
            mBinding.navWallet.setVisibility(View.GONE);
        }


    }

    private void onSavedLocationsClick(View view) {
        if (!isLoggedIn())
            return;
        activity.startActivity(new Intent(activity, FavoritePlacesActivity.class));
    }
    private void onProfileImageClick(View view) {
        if (!isLoggedIn())
            return;
        ImagePicker.create(activity)
                .returnMode(ReturnMode.ALL)
                .folderMode(true)
                .single()
                .limit(1)
                .enableLog(true)
                .start(PICK_PROFILE_IMAGE_FOR_NAV);
    }
    private void onSettingsClick(View view) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));

    }
    private void onLoginClick(View view) {

        activity.startActivity(new Intent(activity, LoginActivity.class));

    }
    private void onLogoutClick(View view) {
        dismiss();
        preferencesManager.removeUser();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
    private void onWalletClick(View v) {
        if (!isLoggedIn())
            return;
        activity.startActivity(new Intent(activity, WalletActivity.class));
    }
    private void onCouponsClick(View v) {
        if (!isLoggedIn())
            return;
        activity.startActivity(new Intent(activity, CouponsActivity.class));
    }
    private void onPaymentMethodsClick(View v) {
        if (!isLoggedIn())
            return;
        activity.startActivity(new Intent(activity, PaymentCardsActivity.class));
    }
    private void onMenuCartClick(View v) {

        activity.startActivity(new Intent(activity, CartActivity.class));
    }
    private void onAboutClick(View v) {
        activity.startActivity(new Intent(activity, AboutUsActivity.class));
    }
    private void onContactUsClick(View v) {
        activity.startActivity(new Intent(activity, Support1Activity.class));

    }
    private void onRegisterAsProviderClick(View v) {
        activity.startActivity(new Intent(activity, ShopRegisterActivity.class));
    }
    private void onRegisterAsAgentClick(View view) {
        activity.startActivity(new Intent(activity, RegisterActivity.class));

    }
    private void onFAQClick(View v) {
        activity.startActivity(new Intent(activity, FAQActivity.class));
    }
    public boolean isLoggedIn() {
        if (preferencesManager.getAuthenticationToken() == null) {

            loginDialog.getDialog().show();
            return false;
        } else
            return true;
    }

    public void show() {
        mBinding.drawerLayout.openDrawer(GravityCompat.START);
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }


    public interface NavigationViewDialogHandler {
        void onImageSelect(String imagePath);
    }

    private NavigationViewDialogHandler mClickHandler;


    public void setNavigationViewDialogHandler(NavigationViewDialogHandler clickHandler) {
        mClickHandler = clickHandler;
    }

}
