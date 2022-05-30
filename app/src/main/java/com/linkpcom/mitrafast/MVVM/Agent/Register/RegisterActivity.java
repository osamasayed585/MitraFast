package com.linkpcom.mitrafast.MVVM.Agent.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Company;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CompaniesResponse;
import com.squareup.picasso.Picasso;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.Dialogs.ImageDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Nationality;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AgentRegisterRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NationalitiesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.RegisterResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Common.ActivateAccount.ActivateAccountActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAgentRegisterBinding;

import java.io.File;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.VERIFY_USER;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends BaseActivity {

    public static final int PICK_PROFILE_IMAGE = 1;
    public static final int PICK_IDENTITY_IMAGE = 2;
    public static final int PICK_DRIVING_LICENSE_IMAGE = 3;
    public static final int PICK_TRAFFIC_LICENSE_FORM_IMAGE = 4;

    int pickedImage;


    ActivityAgentRegisterBinding mBinding;
    RegisterViewModel mViewModel;
    Image profileImage;
    Image identityImage;
    Image drivingLicenseImage;
    Image trafficLicenseFormImage;

    ImageDialog imageDialog;

    ListDialog nationalitiesDialog;
    ListDialog companiesDialog;
    List<Nationality> nationalitiess;
    Nationality selectedNationality;
    List<Company> companies;
    Company selectedCompany = new Company();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getNationalitiesResponseMutableLiveData().observe(this, this::onNationalitiesResponse);
        mViewModel.getCompaniesResponseMutableLiveData().observe(this, this::onCompaniesResponse);
        mViewModel.getRegisterResponseMutableLiveData().observe(this, this::onRegisterResponse);
        mViewModel.getTermsResponseMutableLiveData().observe(this, this::onTermsResponse);

        //New Instances
        imageDialog = new ImageDialog(this);


        mBinding.tvNationality.setOnClickListener(this::onNationalityClick);
        mBinding.tvCompanies.setOnClickListener(this::onCompaniesClick);

        mBinding.ivProfilePic.setOnClickListener(view -> {
            pickedImage = PICK_PROFILE_IMAGE;
            requestImage();
        });
        mBinding.tvIdPic.setOnClickListener(view -> {
            pickedImage = PICK_IDENTITY_IMAGE;
            requestImage();
        });
        mBinding.ivIdPic.setOnClickListener(view -> {
            imageDialog.setImage(identityImage.getPath());
            imageDialog.getDialog().show();
        });
        mBinding.tvLicencePic.setOnClickListener(view -> {
            pickedImage = PICK_DRIVING_LICENSE_IMAGE;
            requestImage();
        });
        mBinding.ivLicencePic.setOnClickListener(view -> {
            imageDialog.setImage(drivingLicenseImage.getPath());
            imageDialog.getDialog().show();
        });
        mBinding.tvCarLicencePic.setOnClickListener(view -> {
            pickedImage = PICK_TRAFFIC_LICENSE_FORM_IMAGE;
            requestImage();
        });
        mBinding.ivCarLicencePic.setOnClickListener(view -> {
            imageDialog.setImage(trafficLicenseFormImage.getPath());
            imageDialog.getDialog().show();
        });
        mBinding.btnRegiter.setOnClickListener(this::onRegisterClick);

        mViewModel.requestNationalities();
        mViewModel.requestCompanies();

        mViewModel.requestTerms(3);
        mBinding.btnNext.setOnClickListener(this::onNextClick);
        mBinding.tvPersonalInformationLabel.setOnClickListener(this::onPersonalInformationLabelClick);
        mBinding.tvConditionsForAcceptingRegistrationLabel.setOnClickListener(this::onConditionsForAcceptingRegistrationLabelClick);

    }

    private void onTermsResponse(TermsAndConditionsResponse response) {
        mBinding.tvConditionsForAcceptingRegistration.setText(response.getData().getTerms());
    }

    private void onPersonalInformationLabelClick(View view) {


        switchContainers(true);
    }

    private void onConditionsForAcceptingRegistrationLabelClick(View view) {
        switchToConditionsForAcceptingRegistrationContainer();
    }


    private void onNextClick(View view) {
        switchToConditionsForAcceptingRegistrationContainer();

    }

    private void switchToConditionsForAcceptingRegistrationContainer() {
        if (!personalInformationValidation())
            return;
        switchContainers(false);
    }

    private void switchContainers(boolean isPersonalInformation) {
        mBinding.tvPersonalInformationLabel.setBackgroundResource(isPersonalInformation ? R.drawable.edged_corner_black_solid : R.drawable.edged_corner_black_stroke);
        mBinding.tvPersonalInformationLabel.setTextColor(getResources().getColor(isPersonalInformation ? R.color.white : R.color.black));
        mBinding.tvConditionsForAcceptingRegistrationLabel.setBackgroundResource(isPersonalInformation ? R.drawable.edged_corner_black_stroke : R.drawable.edged_corner_black_solid);
        mBinding.tvConditionsForAcceptingRegistrationLabel.setTextColor(getResources().getColor(isPersonalInformation ? R.color.black : R.color.white));


        mBinding.llPersonalInformationContainer.setVisibility(isPersonalInformation ? VISIBLE : GONE);
        mBinding.llConditionsForAcceptingRegistrationContainer.setVisibility(isPersonalInformation ? GONE : VISIBLE);
    }


    private void onRegisterResponse(RegisterResponse response) {

        preferencesManager.setAuthenticationToken(response.getAccess_token());
        preferencesManager.setAuthenticationType(response.getToken_type());
        preferencesManager.setUserVerified(response.getData().isUserVerify());
        preferencesManager.setUserId(String.valueOf(response.getData().getId()));
        preferencesManager.setUserTypeId("3");


        Intent intent = new Intent(this, ActivateAccountActivity.class);
        //+966 code For Saudi arabia country
        intent.putExtra(CONSTANTS.INTENTS.MOBILE_PHONE_NUMBER, "+966" + mBinding.etPhone.getText().toString().substring(1));
        startActivityForResult(intent, VERIFY_USER);
    }

    private void onNationalitiesResponse(NationalitiesResponse response) {
        nationalitiesDialog = new ListDialog(this);
        nationalitiess = response.getData();
        nationalitiesDialog.setData(response.getNames());
        nationalitiesDialog.setOnItemClickListener(clickedItemPosition -> {
            selectedNationality = nationalitiess.get(clickedItemPosition);
            mBinding.tvNationality.setText(selectedNationality.getName());
            nationalitiesDialog.getDialog().dismiss();
        });
        nationalitiesDialog.setDismissClickListener(view -> nationalitiesDialog.getDialog().dismiss());
    }

    private void onCompaniesResponse(CompaniesResponse response) {
        companiesDialog = new ListDialog(this);
        companies = response.getData();
        companiesDialog.setData(response.getNames());
        companiesDialog.setOnItemClickListener(clickedItemPosition -> {
            selectedCompany = companies.get(clickedItemPosition);
            mBinding.tvCompanies.setText(selectedCompany.getName());
            companiesDialog.getDialog().dismiss();
        });
        companiesDialog.setDismissClickListener(view -> companiesDialog.getDialog().dismiss());
    }

    public void requestImage() {
        ImagePicker.create(RegisterActivity.this)
                .returnMode(ReturnMode.CAMERA_ONLY) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .single()
                .limit(1) // max images can be selected (99 by default)
                .enableLog(true) // disabling log
                .start(); // start image picker activity with request code


    }

    private void onNationalityClick(View view) {
        nationalitiesDialog.getDialog().show();
    }

    private void onCompaniesClick(View view) {
        companiesDialog.getDialog().show();
    }

    private void onRegisterClick(View view) {
        if (!nationalityInformationValidation())
            return;
        mViewModel.requestRegister(getData());

    }

    private boolean personalInformationValidation() {


        return
                validator.isNotNull(profileImage, getString(R.string.profile_image_message)) &&
                        validator.isNotEmpty(mBinding.etPhone) &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.etPhone.getText().toString(), mBinding.etPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.etPhone.getText().toString(), mBinding.etPhone, 10) &&
                        validator.isNotEmpty(mBinding.etName) &&
                        validator.isNotEmpty(mBinding.etId) &&
                        validator.isNotEmpty(mBinding.etEmail) &&
                        validator.email(mBinding.etEmail) &&
                        validator.isNotEmpty(mBinding.etDay) &&
                        validator.isNotEmpty(mBinding.etMonth) &&
                        validator.isNotEmpty(mBinding.etYear) &&
                        validator.isNotNull(selectedNationality, mBinding.tvNationality) &&

                        validator.isNotEmpty(mBinding.identityName) &&
                        validator.isNotNull(identityImage, getString(R.string.identity_image_message)) &&
                        validator.isNotNull(drivingLicenseImage, getString(R.string.driving_license_image_message)) &&
                        validator.isNotNull(trafficLicenseFormImage, getString(R.string.car_insurance_image_message));


    }

    private boolean nationalityInformationValidation() {


        return
                validator.isNotEmpty(mBinding.etRegisteredNumberInStcPay);


    }


    private AgentRegisterRequest getData() {
        return AgentRegisterRequest.builder()
                .mobile(mBinding.etPhone.getText().toString().substring(1))
                //1 id For Saudi arabia country
                .country_id(1)
                .name(mBinding.etName.getText().toString())
                .identity_id(mBinding.etId.getText().toString())
                .email(mBinding.etEmail.getText().toString())
                .birthday_date(String.format("%s/%s/%s", mBinding.etDay.getText().toString(), mBinding.etMonth.getText().toString(), mBinding.etYear.getText().toString()))
                .nationality_id(selectedNationality.getId())
                .time_type(mBinding.rbFullTime.isChecked() ? "1" : "0")
                .stc_pay_number(mBinding.etRegisteredNumberInStcPay.getText().toString())
                .company_id(selectedCompany != null ? String.valueOf(selectedCompany.getId()) : null)
                .identity_name(mBinding.identityName.getText().toString())

                .image(StaticMethods.getImageRequestBody(profileImage.getPath(), "image"))
                .identity_image(StaticMethods.getImageRequestBody(identityImage.getPath(), "identity_image"))
                .driving_license_image(StaticMethods.getImageRequestBody(drivingLicenseImage.getPath(), "driving_license_image"))
                .car_insurance_image(StaticMethods.getImageRequestBody(trafficLicenseFormImage.getPath(), "car_insurance_image"))
                .build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VERIFY_USER)
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                switch (pickedImage) {
                    case PICK_PROFILE_IMAGE:
                        profileImage = image;
                        File imgFile = new File(profileImage.getPath());
                        if (imgFile.exists())
                            Picasso.get().load(imgFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivProfilePic);
                        break;
                    case PICK_IDENTITY_IMAGE:

                        identityImage = image;
                        imgFile = new File(identityImage.getPath());
                        if (imgFile.exists()) {
                            mBinding.tvIdPic.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            mBinding.tvIdPic.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_image_picked, 0, 0, 0);
                            mBinding.ivIdPic.setVisibility(VISIBLE);
                            Picasso.get().load(imgFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivIdPic);

                        }
                        break;

                    case PICK_DRIVING_LICENSE_IMAGE:
                        drivingLicenseImage = image;
                        imgFile = new File(drivingLicenseImage.getPath());
                        if (imgFile.exists()) {
                            mBinding.tvLicencePic.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            mBinding.tvLicencePic.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_image_picked, 0, 0, 0);
                            mBinding.ivLicencePic.setVisibility(VISIBLE);
                            Picasso.get().load(imgFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivLicencePic);

                        }
                        break;
                    case PICK_TRAFFIC_LICENSE_FORM_IMAGE:
                        trafficLicenseFormImage = image;
                        imgFile = new File(trafficLicenseFormImage.getPath());
                        if (imgFile.exists()) {
                            mBinding.tvCarLicencePic.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            mBinding.tvCarLicencePic.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_image_picked, 0, 0, 0);
                            mBinding.ivCarLicencePic.setVisibility(VISIBLE);
                            Picasso.get().load(imgFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivCarLicencePic);

                        }
                        break;

                }
            }
        }


    }

}