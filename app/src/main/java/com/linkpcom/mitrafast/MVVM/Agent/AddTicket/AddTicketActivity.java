package com.linkpcom.mitrafast.MVVM.Agent.AddTicket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.linkpcom.mitrafast.Classes.Adapters.TicketImagesAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.TicketType;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketReplyRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketTypesResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAgentAddTicketBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTicketActivity extends BaseActivity {
    ActivityAgentAddTicketBinding mBinding;

    private AddTicketViewModel mViewModel;
    private ListDialog ticketTypesDialog;
    private List<TicketType> ticketTypes;
    private TicketType selectedTicketType;
    private TicketImagesAdapter ticketImagesAdapter;
    private Ticket ticket;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentAddTicketBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AddTicketViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getAddTicketResponseMutableLiveData().observe(this, this::onAddTicketResponse);
        mViewModel.getTicketTypesResponseMutableLiveData().observe(this, this::onTicketTypesResponse);
        mViewModel.getAddTicketReplyResponseMutableLiveData().observe(this, this::onRepliesResponse);

        //New Instances
        ticketTypesDialog = new ListDialog(this);
        ticketImagesAdapter = new TicketImagesAdapter();

        mBinding.ticketType.setOnClickListener(this::onTicketTypeClick);
        mBinding.ticketImages.setAdapter(ticketImagesAdapter);
        ticketImagesAdapter.setItemClickListener(this::onAddMoreImagesClick);
        mBinding.etIdPic.setOnClickListener(this::onAddMoreImagesClick);
        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);
        mViewModel.requestTicketTypes();

        ticket = getIntent().getParcelableExtra(CONSTANTS.INTENTS.OBJECT);

        if (ticket != null) {
            mBinding.tvEditName.setText(ticket.getName());
            mBinding.tvEditPhone.setText(String.format("0%s", ticket.getMobile()));
        }
    }

    private void onRepliesResponse(BaseResponse baseResponse) {
        if (baseResponse.isSuccess())
            finish();
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

    private void onTicketTypeClick(View view) {
        ticketTypesDialog.getDialog().show();
    }

    private void onSendButtonClicked(View view) {
        if (!validation())
            return;

        if (ticket != null) {
            mViewModel.requestAddTicketReply(getDataForReply());
        } else {
            mViewModel.requestAddTicket(getData());
        }
    }

    private void onTicketTypesResponse(TicketTypesResponse response) {
        ticketTypes = response.getData();
        ticketTypesDialog.setData(response.getTicketTypeNames());
        ticketTypesDialog.setOnItemClickListener(clickedItemPosition -> {
            selectedTicketType = ticketTypes.get(clickedItemPosition);
            mBinding.ticketType.setText(selectedTicketType.getName());
            ticketTypesDialog.getDialog().dismiss();
        });
        ticketTypesDialog.setDismissClickListener(view -> ticketTypesDialog.getDialog().dismiss());
    }

    private void onAddTicketResponse(BaseResponse response) {
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.the_message_has_been_sent_we_will_contact_you_soon_via_mail) , Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validation() {
        return
                validator.isNotNull(selectedTicketType, getString(R.string.select_ticket_type)) &&
                        validator.isNotEmpty(mBinding.tvEditName) &&
                        validator.isNotEmpty(mBinding.tvEditPhone) &&
                        validator.startWith(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, "05") &&
                        validator.phone(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, 10) &&
                        validator.isNotEmpty(mBinding.tvEditMessage);
    }

    private AddTicketRequest getData() {
        return AddTicketRequest.builder()
                .ticket_type_id(selectedTicketType.getId())
                .name(mBinding.tvEditName.getText().toString())
                .mobile(mBinding.tvEditPhone.getText().toString().substring(1))
                .message(mBinding.tvEditMessage.getText().toString())
                .images(!ticketImagesAdapter.getData().isEmpty() ? StaticMethods.getImagesRequestBody(ticketImagesAdapter.getData()) : null)
                .build();
    }

    private AddTicketReplyRequest getDataForReply() {
        return AddTicketReplyRequest.builder()
                .support_ticket_id(ticket.getId())
                .reply(mBinding.tvEditMessage.getText().toString())
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