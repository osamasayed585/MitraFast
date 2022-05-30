package com.linkpcom.mitrafast.MVVM.Client.Profile;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Common.Login.LoginActivity;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditClientProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ClientProfileResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityClientProfileBinding;

import java.io.File;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileActivity extends BaseActivity {
    ActivityClientProfileBinding mBinding;
    ProfileViewModel mViewModel;

    Image userImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getProfileResponseMutableLiveData().observe(this, this::onProfileResponse);
        mViewModel.getEditProfileResponseMutableLiveData().observe(this, this::onEditProfileResponse);

        mViewModel.requestClientProfile();


        mBinding.save.setOnClickListener(this::onSaveClick);

        mBinding.btnAddImage.setOnClickListener(this::requestImage);
        mBinding.userImage.setOnClickListener(this::requestImage);
        mBinding.btnLogOut.setOnClickListener(this::onLogoutClick);
    }

    private void onLogoutClick(View view) {
        preferencesManager.removeUser();


        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent loginIntent = new Intent(this, LoginActivity.class);


        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntentWithParentStack(mainIntent);
        taskStackBuilder.addNextIntent(loginIntent);
        taskStackBuilder.startActivities();

    }

    public void requestImage(View view) {
        ImagePicker.create(ProfileActivity.this)
                .returnMode(ReturnMode.ALL)
                .folderMode(true)
                .single()
                .limit(1)
                .enableLog(true)
                .start();

    }

    private void onSaveClick(View view) {
        if (!validation())
            return;
        mViewModel.requestEditProfile(getData());
    }

    private EditClientProfileRequest getData() {
        return EditClientProfileRequest.builder()
                .name(mBinding.userName.getText().toString())
                .address(mBinding.address.getText().toString())
                .image(userImage != null ? StaticMethods.getImageRequestBody(userImage.getPath(), "image") : null)
                .build();
    }

    private boolean validation() {

        if (mBinding.address.getVisibility() == View.GONE){
            return validator.isNotEmpty(mBinding.userName);
        }else {
            return validator.isNotEmpty(mBinding.userName) &&
                    validator.isNotEmpty(mBinding.address);
        }
    }


    private void onProfileResponse(ClientProfileResponse response) {
        Picasso.get().load(response.getData().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.userImage);
        mBinding.headerUserName.setText(response.getData().getName());
        mBinding.userName.setText(response.getData().getName());
        mBinding.mobileNumber.setText(response.getData().getMobile());

        if (response.getData().getAddress() != null) {
            mBinding.titleAddress.setVisibility(View.VISIBLE);
            mBinding.address.setVisibility(View.VISIBLE);
            mBinding.address.setText(response.getData().getAddress());
        }


    }

    private void onEditProfileResponse(ClientProfileResponse response) {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

        mBinding.headerUserName.setText(response.getData().getName());


        preferencesManager.setImage(response.getData().getImage());
        preferencesManager.setName(response.getData().getName());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            userImage = ImagePicker.getFirstImageOrNull(data);
            File file = new File(userImage.getPath());
            if (file.exists())
                Picasso.get().load(file).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.userImage);


        }
    }
}
