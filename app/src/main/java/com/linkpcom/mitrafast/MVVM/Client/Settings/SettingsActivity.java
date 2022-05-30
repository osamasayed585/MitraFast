package com.linkpcom.mitrafast.MVVM.Client.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Common.Language.LanguageActivity;
import com.linkpcom.mitrafast.databinding.ActivitySettingsBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SettingsActivity extends BaseActivity {
    ActivitySettingsBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.tvLanguage.setOnClickListener(this::onLanguageClick);
    }

    private void onLanguageClick(View view) {
        startActivity(new Intent(this, LanguageActivity.class));
    }


}