package com.linkpcom.mitrafast.MVVM.Common.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.LoginRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AddressResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.LoginResponse;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.CompleteData.CompleteDataActivity;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Common.ActivateAccount.ActivateAccountActivity;
import com.linkpcom.mitrafast.databinding.ActivityLoginBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.COMPLETE_DATA;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.VERIFY_USER;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Observable;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

    ActivityLoginBinding mBinding;
    LoginViewModel mViewModel;
    private Address address;
    private Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);


        mViewModel.getLoginResponseMutableLiveData().observe(this, this::onLoginResponse);


        mBinding.btnLogin.setOnClickListener(this::onLoginClick);
        geocoder = new Geocoder(this, new Locale(new PreferencesManager(this).getLanguage()));

    }


    private void onLoginClick(View view) {
        if (!validation())
            return;
        mViewModel.requestLogin(getData());
    }


    private void onLoginResponse(LoginResponse response) {

        preferencesManager.setAuthenticationToken(response.getAccess_token());
        preferencesManager.setAuthenticationType(response.getToken_type());
        preferencesManager.setUserId(String.valueOf(response.getData().getId()));
        preferencesManager.setUserTypeId(response.getData().getUser_type_id());
        preferencesManager.setProfileComplete(response.getData().is_data_complete());
        preferencesManager.setImage(response.getData().getImage());
        preferencesManager.setName(response.getData().getName());
        preferencesManager.setAddressId(String.valueOf(response.getAddressId()));


        Intent intent = new Intent(this, ActivateAccountActivity.class);
        //+966 code For Saudi arabia country
        intent.putExtra(CONSTANTS.INTENTS.MOBILE_PHONE_NUMBER, "+966" + mBinding.tvEditPhone.getText().toString().substring(1));
        startActivityForResult(intent, VERIFY_USER);

    }

    private boolean validation() {
        try {
            address = requestAddressName(Double.parseDouble(preferencesManager.getLat()), Double.parseDouble(preferencesManager.getLng()), this);
        } catch (Exception ignored) {

        }

        return
                validator.isNotEmpty(mBinding.tvEditPhone) && address != null &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, 10);
    }

    private LoginRequest getData() {
        return LoginRequest.builder()
                //1 id For Saudi arabia country
                .country_id("1")
                .mobile(mBinding.tvEditPhone.getText().toString().substring(1))
                .lat(preferencesManager.getLat())
                .lng(preferencesManager.getLng())
                .AdminArea(address.getAdminArea())
                .Locality(address.getLocality())
                .SubLocality(address.getSubLocality())
                .address(preferencesManager.getAddress())
                .name(preferencesManager.getAddressName())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == VERIFY_USER) {
                if (preferencesManager.getUserTypeId().equals("2"))
                    if (!preferencesManager.isProfileComplete())
                        startActivityForResult(new Intent(this, CompleteDataActivity.class), COMPLETE_DATA);
                    else {
                        startActivity(new Intent(this,MainActivity.class));
                        finishAffinity();
                    }
                else {
                    Intent intent = new Intent(this, com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

            } else if (requestCode == COMPLETE_DATA) {
                startActivity(new Intent(this,MainActivity.class));
                finishAffinity();

            }
        }
    }

    public Address requestAddressName(double latitude, double longitude, Context context) throws IOException {

        if (Geocoder.isPresent()) {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0);
        }

        return null;
    }


}