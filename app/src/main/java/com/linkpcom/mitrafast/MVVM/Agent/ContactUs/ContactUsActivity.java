package com.linkpcom.mitrafast.MVVM.Agent.ContactUs;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.AgentTicketsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ReplyResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.AddTicket.AddTicketActivity;
import com.linkpcom.mitrafast.databinding.ActivityAgentContactUsBinding;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint
public class ContactUsActivity extends BaseActivity {
    ActivityAgentContactUsBinding mBinding;
    private ContactUsViewModel mViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    AgentTicketsAdapter agentTicketsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentContactUsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getTicketsResponseMutableLiveData().observe(this, this::onTicketsResponse);
        mViewModel.getReplyResponseMutableLiveData().observe(this, this::onRepliesResponse);

        //New Instance
        agentTicketsAdapter = new AgentTicketsAdapter();

        mBinding.tickets.setAdapter(agentTicketsAdapter);

        agentTicketsAdapter.setItemClickListener(this::onReplayClick);

        agentTicketsAdapter.itemClickStream.subscribe(integer -> {
            mViewModel.requestAgentReply(integer);
        });

        mBinding.openNewTicket.setOnClickListener(this::onOpenNewTicketClick);

        mViewModel.requestTickets();
    }

    private void onRepliesResponse(ReplyResponse replyResponse) {
        agentTicketsAdapter.setReply(replyResponse.getData());
    }

    private void onReplayClick(Ticket ticket) {
        startActivity(new Intent(this, AddTicketActivity.class)
                .putExtra(OBJECT, ticket));
    }

    private void onOpenNewTicketClick(View view) {
        startActivity(new Intent(this, AddTicketActivity.class));
    }

    private void onDeleteClick(Ticket ticket) {
        mViewModel.requestDeleteTicket(ticket.getId());

    }

    private void onTicketsResponse(TicketsResponse response) {
        agentTicketsAdapter.setData(response.getData());
    }

}
