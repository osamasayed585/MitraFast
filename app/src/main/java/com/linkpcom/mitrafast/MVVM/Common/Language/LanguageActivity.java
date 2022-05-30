package com.linkpcom.mitrafast.MVVM.Common.Language;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Common.SplashScreen.SplashScreenActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityLanguageBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LanguageActivity extends BaseActivity {
    ActivityLanguageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.btnArabic.setOnClickListener(this::onArabicBtnClick);
        mBinding.btnEnglish.setOnClickListener(this::onEnglishBtnClick);

        String language = getResources().getConfiguration().locale.getLanguage();

        if (language.equals("en")) {
            mBinding.btnEnglish.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.us, 0, R.drawable.cheched, 0);
        } else if (language.equals("ar")) {
            mBinding.btnArabic.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.palastien, 0, R.drawable.cheched, 0);
        }
    }

    private void onArabicBtnClick(View view) {
        if (!preferencesManager.getLanguage().equals("ar")) {
            preferencesManager.setLanguage("ar");
            startSplashScreenActivity();
        }
    }

    private void onEnglishBtnClick(View view) {
        if (!preferencesManager.getLanguage().equals("en")) {
            preferencesManager.setLanguage("en");
            startSplashScreenActivity();
        }
    }

    private void startSplashScreenActivity() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}