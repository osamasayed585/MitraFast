package com.linkpcom.mitrafast.MVVM.Client.AddTicket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.linkpcom.mitrafast.Classes.Adapters.TicketImagesAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Support;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityClientAddTicketBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTicketActivity extends BaseActivity {
    ActivityClientAddTicketBinding mBinding;
    private AddTicketViewModel mViewModel;


    MessageDialog messageDialog;

    TicketImagesAdapter ticketImagesAdapter;

    private Support supportSection;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientAddTicketBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        supportSection = getIntent().getParcelableExtra(OBJECT);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AddTicketViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getAddTicketResponseMutableLiveData().observe(this, this::onAddTicketResponse);


        //New Instances
        messageDialog = new MessageDialog(this);
        ticketImagesAdapter = new TicketImagesAdapter();


        mBinding.tvSubject.setText(supportSection.getName());

        mBinding.ticketImages.setAdapter(ticketImagesAdapter);
        ticketImagesAdapter.setItemClickListener(this::onAddMoreImagesClick);
        mBinding.etIdPic.setOnClickListener(this::onAddMoreImagesClick);

        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);

    }

    public void onAddMoreImagesClick(View view) {
        ImagePicker.create(this)
                .returnMode(ReturnMode.CAMERA_ONLY)
                .folderMode(true)
                .limit(20)
                .showCamera(true)
                .enableLog(true)
                .start();

    }


    private void onSendButtonClicked(View view) {
        if (!validation())
            return;
        mViewModel.requestAddTicket(getData());
    }


    private void onAddTicketResponse(BaseResponse response) {
        messageDialog.getDialog().show();
        messageDialog.setMessageDialogHandler(this::finish);
    }

    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.tvEditName) &&
                        validator.isNotEmpty(mBinding.tvEditPhone) &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, 10) &&
                        validator.isNotEmpty(mBinding.tvEmail) &&
                        validator.isNotEmpty(mBinding.tvEditMessage);


    }

    private AddTicketRequest getData() {
        return AddTicketRequest.builder()
                .support_section_id(supportSection.getId())
                .name(mBinding.tvEditName.getText().toString())
                .mobile(mBinding.tvEditPhone.getText().toString().substring(1))
                .message(mBinding.tvEditMessage.getText().toString())
                .email(mBinding.tvEmail.getText().toString())
                .images(!ticketImagesAdapter.getData().isEmpty() ? StaticMethods.getImagesRequestBody(ticketImagesAdapter.getData()) : null)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ticketImagesAdapter.setData(ImagePicker.getImages(data));
        }
    }
}