package com.linkpcom.mitrafast.MVVM.Client.AddTicketReply;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.linkpcom.mitrafast.Classes.Adapters.TicketImagesAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketReplyRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAddTicketReplyBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddTicketReplyActivity extends BaseActivity {
    ActivityAddTicketReplyBinding mBinding;
    private AddTicketReplyViewModel mViewModel;

    MessageDialog messageDialog;

    TicketImagesAdapter ticketReplyImagesAdapter;

    private Ticket ticket;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddTicketReplyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ticket = getIntent().getParcelableExtra(OBJECT);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(AddTicketReplyViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getAddTicketReplyResponseMutableLiveData().observe(this, this::onAddTicketReplyResponse);


        //New Instances
        ticketReplyImagesAdapter = new TicketImagesAdapter();

        mBinding.ticketImages.setAdapter(ticketReplyImagesAdapter);

        ticketReplyImagesAdapter.setItemClickListener(this::onAddMoreImagesClick);
        mBinding.etIdPic.setOnClickListener(this::onAddMoreImagesClick);
        mBinding.tvSubject.setText(String.format("%s %s", getText(R.string.type_ticket), ticket.getSupport_section().getName()));
        mBinding.tvOrderNumber.setText(String.valueOf(ticket.getId()));
        mBinding.tvRequestNumber.setText(String.format("#%s %s", getString(R.string.request_number), ticket.getId()));
        mBinding.tvRequestDate.setText(String.format("%s %s", getText(R.string.request_date), ticket.getTime()));
        if (getResources().getConfiguration().locale.getLanguage().equals("en"))
            mBinding.tvRequestStatus.setText(Html.fromHtml(String.format("%s %s", getText(R.string.request_status), getColoredSpanned(ticket.getSupport_ticket_statuses().getName_en(),ticket.getSupport_ticket_statuses().getStatus_color()) )));
        else
            mBinding.tvRequestStatus.setText(Html.fromHtml(String.format("%s %s", getText(R.string.request_status),getColoredSpanned(ticket.getSupport_ticket_statuses().getName_ar(),ticket.getSupport_ticket_statuses().getStatus_color()) )));
        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return  input;
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
        mViewModel.requestAddTicketReply(getData());

    }


    private void onAddTicketReplyResponse(BaseResponse response) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.the_message_has_been_sent_we_will_contact_you_soon_via_mail), Toast.LENGTH_SHORT).show();
        finish();
    }


    private boolean validation() {


        return
                validator.isNotEmpty(mBinding.tvReply);

    }

    private AddTicketReplyRequest getData() {
        Log.d("Osama_osama", "getData: " + (ticketReplyImagesAdapter.getData()));
        return AddTicketReplyRequest.builder()
                .support_ticket_id(ticket.getId())
                .reply(mBinding.tvReply.getText().toString())
                .images(!ticketReplyImagesAdapter.getData().isEmpty() ? StaticMethods.getImagesRequestBody(ticketReplyImagesAdapter.getData()) : null)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ticketReplyImagesAdapter.setData(ImagePicker.getImages(data));
        }
    }

}