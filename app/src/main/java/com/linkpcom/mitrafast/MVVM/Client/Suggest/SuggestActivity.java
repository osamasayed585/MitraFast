package com.linkpcom.mitrafast.MVVM.Client.Suggest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SuggestRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.databinding.ActivitySuggestBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SuggestActivity extends BaseActivity {
    ActivitySuggestBinding mBinding;
    private SuggestViewModel mViewModel;


    MessageDialog messageDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySuggestBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(SuggestViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getSuggestResponseMutableLiveData().observe(this, this::onSuggestResponse);


        //New Instances
        messageDialog = new MessageDialog(this);

        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);


    }



    private void onSendButtonClicked(View view) {
        if (!validation())
            return;
        mViewModel.requestSuggest(getData());
    }


    private void onSuggestResponse(BaseResponse response) {
        messageDialog.getDialog().show();
        messageDialog.setMessageDialogHandler(this::finish);
    }


    private boolean validation() {


        return
                        validator.isNotEmpty(mBinding.tvEditName) &&
                        validator.isNotEmpty(mBinding.tvEmail) &&
                        validator.isNotEmpty(mBinding.tvEditPhone) &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, 10) &&
                        validator.isNotEmpty(mBinding.tvEditMessage);


    }

    private SuggestRequest getData() {
        return SuggestRequest.builder()
                //1 id For Saudi arabia country
                .country_id("1")
                .name(mBinding.tvEditName.getText().toString())
                .mobile(mBinding.tvEditPhone.getText().toString().substring(1))
                .message(mBinding.tvEditMessage.getText().toString())
                .email(mBinding.tvEditMessage.getText().toString())
                .build();
    }

}
