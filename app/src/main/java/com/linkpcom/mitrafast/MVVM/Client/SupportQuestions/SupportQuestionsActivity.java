package com.linkpcom.mitrafast.MVVM.Client.SupportQuestions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.FAQsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Support;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FAQResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.AddTicket.AddTicketActivity;
import com.linkpcom.mitrafast.databinding.ActivitySupportQuestionsBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SupportQuestionsActivity extends BaseActivity {
    ActivitySupportQuestionsBinding mBinding;
    private SupportQuestionsViewModel mViewModel;

    FAQsAdapter supportQuestionsAdapter;
    private Support supportSection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySupportQuestionsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        supportSection = getIntent().getParcelableExtra(OBJECT);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(SupportQuestionsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        // Observer
        mViewModel.getFAQResponseMutableLiveData().observe(this, this::onFAQResponse);

        //New Instance
        supportQuestionsAdapter = new FAQsAdapter();

        mBinding.tvTitle.setText(supportSection.getName());

        mBinding.rvSupportQuestions.setAdapter(supportQuestionsAdapter);

        mBinding.tvHaveAnotherProblem.setOnClickListener(this::onHaveAnotherProblemClick);

        mViewModel.requestFAQ(supportSection.getId());

    }

    private void onHaveAnotherProblemClick(View view) {
        if (!isLoggedIn())
            return;
        Intent intent = new Intent(this, AddTicketActivity.class);
        intent.putExtra(OBJECT, supportSection);
        startActivity(intent);
    }


    private void onFAQResponse(FAQResponse response) {
        supportQuestionsAdapter.setData(response.getData());
    }

}
