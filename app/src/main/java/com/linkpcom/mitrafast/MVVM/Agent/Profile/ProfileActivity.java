package com.linkpcom.mitrafast.MVVM.Agent.Profile;


import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditAgentProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentProfileResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAgentProfileBinding;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileActivity extends BaseActivity {
    ActivityAgentProfileBinding mBinding;
    ProfileViewModel mViewModel;

    Image userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getProfileResponseMutableLiveData().observe(this, this::onProfileResponse);
        mViewModel.getEditProfileResponseMutableLiveData().observe(this, this::onEditProfileResponse);

        mViewModel.requestProfile();

        mBinding.save.setOnClickListener(this::onSaveClick);

    }

    private void onSaveClick(View view) {
        if (!validation())
            return;
        mViewModel.requestEditProfile(getData());

    }

    private EditAgentProfileRequest getData() {
        return EditAgentProfileRequest.builder()
                .identity_name(mBinding.identityName.getText().toString())
                .name(mBinding.name.getText().toString())
                .identity_id(mBinding.identityNumber.getText().toString())
                .email(mBinding.email.getText().toString())
                .image(userImage != null ? StaticMethods.getImageRequestBody(userImage.getPath(), "image") : null)
                .build();
    }

    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.name) &&
                        validator.isNotEmpty(mBinding.identityName) &&
                        validator.isNotEmpty(mBinding.identityNumber) &&
                        validator.isNotEmpty(mBinding.email) &&
                        validator.email(mBinding.email);


    }


    private void onProfileResponse(AgentProfileResponse response) {
        Picasso.get().load(response.getData().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.userImage);
        mBinding.headerUserName.setText(response.getData().getName());
        mBinding.membershipNumber.setText(response.getData().getMembership_number());
        mBinding.name.setText(response.getData().getName());
        mBinding.identityName.setText(response.getData().getIdentity_name());
        mBinding.mobileNumber.setText(response.getData().getMobile());
        mBinding.identityNumber.setText(response.getData().getIdentity_id());
        mBinding.email.setText(response.getData().getEmail());

    }

    private void onEditProfileResponse(AgentProfileResponse response) {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

        mBinding.headerUserName.setText(response.getData().getName());

        preferencesManager.setImage(response.getData().getImage());
        preferencesManager.setName(response.getData().getName());
    }
}