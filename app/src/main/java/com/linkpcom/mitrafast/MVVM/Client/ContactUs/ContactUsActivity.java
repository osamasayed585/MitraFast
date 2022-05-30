package com.linkpcom.mitrafast.MVVM.Client.ContactUs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.MessageDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Subject;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ContactUsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SubjectsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityClientContactUsBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ContactUsActivity extends BaseActivity {
    private ActivityClientContactUsBinding mBinding;
    private ContactUsViewModel mViewModel;


    MessageDialog messageDialog;

    ListDialog subjectsDialog;
    List<Subject> subjects;
    Subject selectedSubject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityClientContactUsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getContactUsResponseMutableLiveData().observe(this, this::onContactUsResponse);
        mViewModel.getSubjectsResponseMutableLiveData().observe(this, this::onSubjectsResponse);


        //New Instances
        messageDialog = new MessageDialog(this);
        subjectsDialog = new ListDialog(this);
        mBinding.tvSubject.setOnClickListener(this::onSubjectClick);

        mBinding.btnSend.setOnClickListener(this::onSendButtonClicked);

        mViewModel.requestSubjects();

    }

    private void onSubjectsResponse(SubjectsResponse response) {
        subjects = response.getData();
        subjectsDialog.setData(response.getNames());
        subjectsDialog.setOnItemClickListener(clickedItemPosition -> {
            selectedSubject = subjects.get(clickedItemPosition);
            mBinding.tvSubject.setText(selectedSubject.getName());
            subjectsDialog.getDialog().dismiss();
        });
        subjectsDialog.setDismissClickListener(view -> subjectsDialog.getDialog().dismiss());
    }

    private void onSubjectClick(View view) {
        subjectsDialog.getDialog().show();
    }


    private void onSendButtonClicked(View view) {
        if (!validation())
            return;
        mViewModel.requestContactUs(getData());
    }


    private void onContactUsResponse(BaseResponse response) {
        messageDialog.getDialog().show();
        messageDialog.setMessageDialogHandler(this::finish);
    }


    private boolean validation() {


        return
                validator.isNotNull(selectedSubject, getString(R.string.select_subject)) &&
                        validator.isNotEmpty(mBinding.tvEditName) &&
                        validator.isNotEmpty(mBinding.tvEditPhone) &&
                        //05 start_with For Saudi arabia country
                        validator.startWith(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, "05") &&
                        //10 number_count For Saudi arabia country
                        validator.phone(mBinding.tvEditPhone.getText().toString(), mBinding.tvEditPhone, 10) &&
                        validator.isNotEmpty(mBinding.tvEmail) &&
                        validator.isNotEmpty(mBinding.tvEditMessage);


    }

    private ContactUsRequest getData() {
        return ContactUsRequest.builder()
                //1 id For Saudi arabia country
                .country_id("1")
                .name(mBinding.tvEditName.getText().toString())
                .mobile(mBinding.tvEditPhone.getText().toString().substring(1))
                .message(mBinding.tvEditMessage.getText().toString())
                .email(mBinding.tvEmail.getText().toString())
                .contact_us_subject_id(selectedSubject.getId())
                .build();
    }

}
