package com.linkpcom.mitrafast.MVVM.Client.CompleteData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.Dialogs.TermsDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CompleteDataRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.RegisterResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityCompleteDataBinding;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class CompleteDataActivity extends BaseActivity {

    ActivityCompleteDataBinding mBinding;
    CompleteDataViewModel mViewModel;

    Image image;

    TermsDialog termsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCompleteDataBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(CompleteDataViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        termsDialog = new TermsDialog(this);

        mViewModel.getCompleteDataResponseMutableLiveData().observe(this, this::onCompleteDataResponse);
        mViewModel.getTermsResponseMutableLiveData().observe(this, this::onTermsResponse);


        mBinding.btnRegister.setOnClickListener(this::onRegisterClick);

        mBinding.btnAddImage.setOnClickListener(this::requestImage);
        mBinding.ivProfilePic.setOnClickListener(this::requestImage);
        mBinding.etTermAndConditions.setOnClickListener(this::onTermsAndConditionsClick);

    }

    private void onTermsResponse(TermsAndConditionsResponse response) {
        termsDialog.setContent(response.getData().getTerms());
        termsDialog.show();
    }

    private void onTermsAndConditionsClick(View view) {
        mViewModel.requestTerms(2);
    }

    private void onRegisterClick(View view) {
        if (!validation())
            return;
        mViewModel.requestCompleteData(getData());

    }


    private void onCompleteDataResponse(RegisterResponse response) {
        preferencesManager.setProfileComplete(true);
        preferencesManager.setName(response.getData().getName());
        preferencesManager.setImage(response.getData().getImage());
        setResult(Activity.RESULT_OK);
        finish();
    }

    //Common Between Activities
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //To Clear Access Token If He Didn't Complete Activation Process
        if (!preferencesManager.isProfileComplete())
            preferencesManager.removeUser();
    }

    private boolean validation() {



        return

                validator.isNotEmpty(mBinding.name) &&
                        validator.isChecked(mBinding.cbAcceptTermAndConditions, getString(R.string.agree_on_terms_and_conditions));

    }

    private CompleteDataRequest getData() {
        return CompleteDataRequest.builder()
                //1 id For Saudi arabia country
                .country_id("1")
                .name(mBinding.name.getText().toString())
                .image(image != null ? StaticMethods.getImageRequestBody(image.getPath(), "image") : null)
                .build();
    }


    public void requestImage(View view) {
        ImagePicker.create(CompleteDataActivity.this)
                .returnMode(ReturnMode.ALL)
                .folderMode(true)
                .single()
                .limit(1)
                .enableLog(true)
                .start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            image = ImagePicker.getFirstImageOrNull(data);
            File file = new File(image.getPath());
            if (file.exists())
                Picasso.get().load(file).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivProfilePic);
        }
    }


}