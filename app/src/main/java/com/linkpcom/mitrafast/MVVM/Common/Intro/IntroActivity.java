package com.linkpcom.mitrafast.MVVM.Common.Intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.linkpcom.mitrafast.Classes.Adapters.IntroSlidersAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.IntroSlider;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.IntroSlidersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity;
import com.linkpcom.mitrafast.MVVM.Client.PickLocation.PickLocationActivity;
import com.linkpcom.mitrafast.databinding.ActivityIntroBinding;

import java.util.List;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.PICK_LOCATION;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class IntroActivity extends BaseActivity {
    ActivityIntroBinding mBinding;
    IntroViewModel mViewModel;

    List<IntroSlider> introSliders;
    @Inject
    IntroSlidersAdapter introSlidersAdapter;
    @Inject
    PagerSnapHelper pagerSnapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(IntroViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getIntroSliderResponseMutableLiveData().observe(this, this::onIntroSlidersResponse);


        pagerSnapHelper.attachToRecyclerView(mBinding.rvIntro);
        mBinding.rvIntro.setAdapter(introSlidersAdapter);
        mBinding.btnNext.setOnClickListener(this::onNextBtnClick);
        mBinding.tvSkip.setOnClickListener(v -> {
            launchHomeScreen();
        });

        if (preferencesManager.getUserTypeId().equals("") || preferencesManager.getUserTypeId().equals("2"))
            mViewModel.requestSliders();
        else {
            finish();
            startActivity(new Intent(this, com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity.class));

        }

    }

    private void onIntroSlidersResponse(IntroSlidersResponse response) {
        introSliders = response.getData();
        introSlidersAdapter.setData(introSliders);
        mBinding.circleIndicator.attachToRecyclerView(mBinding.rvIntro, pagerSnapHelper);
    }


    private void onNextBtnClick(View view) {
        if (introSliders == null)
            return;

        int currentPosition = ((LinearLayoutManager) mBinding.rvIntro.getLayoutManager()).findFirstVisibleItemPosition();
        if (currentPosition < introSliders.size() - 1) {
            mBinding.rvIntro.smoothScrollToPosition(currentPosition + 1);
        } else {
            launchHomeScreen();
        }
    }


    private void launchHomeScreen() {
        if (preferencesManager.getAuthenticationToken() == null || preferencesManager.getAddress() == null)
            startActivityForResult(new Intent(this, PickLocationActivity.class), PICK_LOCATION);
        else {
            finish();
            startActivity(new Intent(this, com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity.class));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_LOCATION) {
                finish();
                startActivity(new Intent(this, com.linkpcom.mitrafast.MVVM.Client.Main.MainActivity.class));
            }
        } else {
            startActivityForResult(new Intent(this, PickLocationActivity.class), PICK_LOCATION);
        }
    }

}