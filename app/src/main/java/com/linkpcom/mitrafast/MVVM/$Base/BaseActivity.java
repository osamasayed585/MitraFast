package com.linkpcom.mitrafast.MVVM.$Base;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.REQUEST_CODE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.SELECT_DELIVERY_LOCATION;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.ErrorHandlerDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritePlace;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.Classes.Utils.Validator;
import com.linkpcom.mitrafast.MVVM.Client.FavoritePlaces.FavoritePlacesActivity;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Client.PickLocation.PickLocationActivity;
import com.linkpcom.mitrafast.MVVM.Common.Login.LoginActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityBaseBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {

    @Inject
    public Validator validator;
    @Inject
    public PreferencesManager preferencesManager;
    @Inject
    public ErrorHandlerDialog errorHandlerDialog;
    @Inject
    public ConfirmDialog loginDialog;

    public SpinKitView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticMethods.setLocale(this, preferencesManager.getLanguage());
        loginDialog.setTitle(getString(R.string.login_first_message));
        loginDialog.setConfirmListDialogHandler(() -> startActivity(new Intent(this, LoginActivity.class)));

    }


    @Override
    public void setContentView(View view) {
        ActivityBaseBinding binding = ActivityBaseBinding.inflate(getLayoutInflater());
        progressView = binding.progressView;
        binding.fContainer.addView(view);
        super.setContentView(binding.getRoot());
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onLoading(boolean isLoading) {
        hideKeyboard();
        progressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    public void onApiError(BaseResponse response) {
        errorHandlerDialog.setBaseResponse(response, this::onErrorHandlerDialogOkClick);
        errorHandlerDialog.show();
    }

    private void onErrorHandlerDialogOkClick(DialogInterface dialog, int which) {
        errorHandlerDialog.dismiss();
        finish();
        startActivity(getIntent());
    }

    public void onBackClick(View view) {
        finish();
    }

    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean isLoggedIn() {
        if (preferencesManager.getAuthenticationToken() == null) {
            loginDialog.getDialog().show();
            return false;
        } else {
            return true;
        }
    }

    public void onTitleClick(View view) {
        if (preferencesManager.getAuthenticationToken() == null)
            startActivity(new Intent(this, PickLocationActivity.class));
        else {
            Intent intent = new Intent(this, FavoritePlacesActivity.class);
            intent.putExtra(REQUEST_CODE, REQUEST_CODE);
            startActivityForResult(intent, SELECT_DELIVERY_LOCATION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_DELIVERY_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            FavoritePlace favoritePlace = data.getParcelableExtra(OBJECT);
            preferencesManager.setLat(favoritePlace.getLat());
            preferencesManager.setLng(favoritePlace.getLng());
            preferencesManager.setAddress(favoritePlace.getAddress());
            preferencesManager.setAddressName(favoritePlace.getName());
            preferencesManager.setAddressId(String.valueOf(favoritePlace.getId()));
        }

    }
}
