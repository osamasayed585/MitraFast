package com.linkpcom.mitrafast.MVVM.Client.Tickets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.ClientReplyAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.ClientReplyImagesAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.ClientTicketsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ReplyResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.AddTicketReply.AddTicketReplyActivity;
import com.linkpcom.mitrafast.databinding.ActivityClientTicketsBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class TicketsActivity extends BaseActivity {
    ActivityClientTicketsBinding mBinding;
    private TicketsViewModel mViewModel;
    private ClientReplyAdapter clientReplyAdapter;

    private ClientTicketsAdapter clientTicketsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientTicketsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getTicketsResponseMutableLiveData().observe(this, this::onFAQResponse);
        mViewModel.getReplyResponseMutableLiveData().observe(this, this::onReplyResponse);

        //New Instance
        clientTicketsAdapter = new ClientTicketsAdapter();
        clientTicketsAdapter.setItemClickListener(this::onReplayClick);
        clientTicketsAdapter.itemClickStream.subscribe(supportTicketId -> mViewModel.requestClientReply(supportTicketId));

        ClientReplyImagesAdapter imagesAdapter = new ClientReplyImagesAdapter();
        imagesAdapter.onClickImage.subscribe(s -> {
        });

        mBinding.rvTickets.setAdapter(clientTicketsAdapter);

        mViewModel.requestClientTickets();

    }

    private void onReplyResponse(ReplyResponse replyResponse) {
        clientTicketsAdapter.setReply(replyResponse.getData());
    }

    private void onReplayClick(Ticket ticket) {
        Intent intent = new Intent(this, AddTicketReplyActivity.class);
        intent.putExtra(OBJECT, ticket);
        startActivity(intent);
    }


    private void onFAQResponse(TicketsResponse response) {
        clientTicketsAdapter.setData(response.getData());
    }

}
