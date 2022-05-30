package com.linkpcom.mitrafast.MVVM.Shop.ShopRegister;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.COMMERCIAL_REGISTER;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID_PHOTO;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.PROFILE_PIC;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopRegisterRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Shop.PickLocation.PickLocationActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityShopRegisterBinding;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ShopRegisterActivity extends BaseActivity {

    private ActivityShopRegisterBinding mBinding;
    private ShopRegisterViewModel mViewModel;
    private String address;
    private LatLng latLng;

    MessageDialog messageDialog;

    Image image, commercialImage, idPhoto;

    private TimePickerDialog mTimePickerDialog;
    SimpleDateFormat simpleTimeFormat;

    private Calendar startTime;
    private Calendar endTime;

    private static final int startTimeFlag = 1;
    private static final int endTimeFlag = 2;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShopRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ShopRegisterViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getRegisterShopResponseMutableLiveData().observe(this, this::onShopRegisterResponse);

        //New Instances
        messageDialog = new MessageDialog(this);


        mBinding.btnSend.setOnClickListener(this::onRegisterButtonClick);
        mBinding.selectLocation.setOnClickListener(this::onLocationClick);

        mBinding.from.setOnClickListener(this::onFromClick);
        mBinding.to.setOnClickListener(this::onToClick);

        mBinding.ivProfilePic.setOnClickListener(this::requestImage);

        mBinding.commercialRegister.setOnClickListener(v -> {
            ImagePicker.create(ShopRegisterActivity.this)
                    .returnMode(ReturnMode.CAMERA_ONLY)
                    .folderMode(true)
                    .single()
                    .limit(1)
                    .enableLog(true)
                    .start(COMMERCIAL_REGISTER);
        });
        mBinding.idPhoto.setOnClickListener(v -> {

            ImagePicker.create(ShopRegisterActivity.this)
                    .returnMode(ReturnMode.CAMERA_ONLY)
                    .folderMode(true)
                    .single()
                    .limit(1)
                    .enableLog(true)
                    .start(ID_PHOTO);
        });
    }

    public void requestImage(View view) {
        ImagePicker.create(ShopRegisterActivity.this)
                .returnMode(ReturnMode.CAMERA_ONLY) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .single()
                .limit(1) // max images can be selected (99 by default)
                .enableLog(true) // disabling log
                .start(PROFILE_PIC); // start image picker activity with request code


    }

    private void onFromClick(View view) {
        flag = startTimeFlag;
        showTimePicker(0, 0, 0);
    }

    private void onToClick(View view) {
        if (startTime != null) {
            flag = endTimeFlag;
            showTimePicker(startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE) + 30, startTime.get(Calendar.SECOND));
        } else {
            Toast.makeText(this, getString(R.string.select_start_work_time_first), Toast.LENGTH_SHORT).show();
        }
    }

    private void onShopRegisterResponse(BaseResponse response) {
        messageDialog.getDialog().show();
        messageDialog.setMessageDialogHandler(this::finish);
    }

    private void onLocationClick(View view) {
        Intent intent = new Intent(this, PickLocationActivity.class);
        startActivityForResult(intent, CONSTANTS.INTENTS.PICK_SHOP_LOCATION);

    }

    private void onRegisterButtonClick(View view) {
        if (!validation())
            return;
        mViewModel.requestShopRegistration(getData());

    }

    private void showTimePicker(int minHour, int minMinute, int minSecond) {
        Calendar now = Calendar.getInstance();
        mTimePickerDialog = TimePickerDialog.newInstance(
                this::onTimeSet,
                now.get(Calendar.HOUR_OF_DAY), // Initial year selection
                now.get(Calendar.MINUTE), // Initial month selection
                false // Inital day selection
        );
        mTimePickerDialog.setMinTime(minHour, minMinute, minSecond);
        mTimePickerDialog.show(getSupportFragmentManager(), "Timepickerdialog");
    }

    private void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int second) {
        switch (flag) {

            case startTimeFlag: {
                if (startTime == null)
                    startTime = Calendar.getInstance();
                setCalendarTime(startTime, mBinding.from, hourOfDay, minute, second);
                break;
            }
            case endTimeFlag: {
                if (endTime == null)
                    endTime = Calendar.getInstance();
                setCalendarTime(endTime, mBinding.to, hourOfDay, minute, second);
                break;
            }
        }
    }

    private void setCalendarTime(Calendar calendar, TextView textView, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.HOUR_OF_DAY, year);
        calendar.set(Calendar.MINUTE, monthOfYear);
        calendar.set(Calendar.SECOND, dayOfMonth);
        textView.setText(getTime(calendar.getTimeInMillis()));
    }

    public String getTime(long time) {
        if (simpleTimeFormat == null)
            simpleTimeFormat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = new Date(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date != null ? simpleTimeFormat.format(date) : String.valueOf(time);
    }

    private boolean validation() {



        return
                validator.isNotNull(image, getString(R.string.select_profile_image)) &&
                        validator.isNotEmpty(mBinding.etEditNameAr) &&
                        validator.isNotEmpty(mBinding.etEditNameEn) &&
                        validator.isNotEmpty(mBinding.etCommercialRegistrationNo) &&
                        validator.isNotEmpty(mBinding.etPhone) &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.etPhone.getText().toString(), mBinding.etPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.etPhone.getText().toString(), mBinding.etPhone, 10) &&
                        validator.isNotEmpty(mBinding.etStoreNo) &&
                        validator.isNotNull(startTime, getString(R.string.select_start_work_time)) &&
                        validator.isNotNull(endTime, getString(R.string.select_end_work_time)) &&
                        validator.isNotNull(address, getString(R.string.select_location));


    }

    private ShopRegisterRequest getData() {
        return ShopRegisterRequest.builder()
                .name_ar(mBinding.etEditNameAr.getText().toString())
                .name_en(mBinding.etEditNameEn.getText().toString())
                .commercial_register(mBinding.etCommercialRegistrationNo.getText().toString())
                .owner_mobile(mBinding.etPhone.getText().toString())
                .mobile(mBinding.etStoreNo.getText().toString())
                .lat(String.valueOf(latLng.latitude))
                .lng(String.valueOf(latLng.longitude))
                .address(String.valueOf(address))
                .work_start_at(String.valueOf(startTime.getTimeInMillis()))
                .end_start_at(String.valueOf(endTime.getTimeInMillis()))
                .value_added_number(Long.parseLong(mBinding.etValueAddedNo.getText().toString()))
                .image(StaticMethods.getImageRequestBody(image.getPath(),"image"))
                .commercial_registe_image(StaticMethods.getImageRequestBody(commercialImage.getPath(),"commercial_registe_image"))
                .owner_identity_image(StaticMethods.getImageRequestBody(idPhoto.getPath(),"owner_identity_image"))
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANTS.INTENTS.PICK_SHOP_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            address = data.getStringExtra(CONSTANTS.INTENTS.ADDRESS);
            latLng = data.getParcelableExtra(CONSTANTS.INTENTS.LATLNG);
        }

        if (requestCode == PROFILE_PIC) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            if (image != null) {
                this.image = image;
                File imgFile = new File(image.getPath());
                if (imgFile.exists())
                    Picasso.get().load(imgFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.ivProfilePic);
            }
        }

        if (requestCode == COMMERCIAL_REGISTER) {
            Image commercialImage = ImagePicker.getFirstImageOrNull(data);
            if (commercialImage != null) {
                this.commercialImage = commercialImage;
                File commercialPhotoFile = new File(commercialImage.getPath());
                if (commercialPhotoFile.exists())
                    Picasso.get().load(commercialPhotoFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.commercialRegister);
            }
        }

        if (requestCode == ID_PHOTO) {
            Image idPhoto = ImagePicker.getFirstImageOrNull(data);
            if (idPhoto != null) {
                this.idPhoto = idPhoto;
                File idPhotoFile = new File(idPhoto.getPath());
                if (idPhotoFile.exists())
                    Picasso.get().load(idPhotoFile).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.idPhoto);
            }
        }

    }

}