package com.linkpcom.mitrafast.MVVM.Client.Support1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.Suggest.SuggestActivity;
import com.linkpcom.mitrafast.MVVM.Client.Support2.Support2Activity;
import com.linkpcom.mitrafast.databinding.ActivitySupport1Binding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Support1Activity extends BaseActivity {
    ActivitySupport1Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySupport1Binding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.tvHelpAndSupport.setOnClickListener(this::onHelpAndSupportClick);
        mBinding.tvSuggest.setOnClickListener(this::onSuggestClick);
    }

    private void onSuggestClick(View view) {
        startActivity(new Intent(this, SuggestActivity.class));
    }

    private void onHelpAndSupportClick(View view) {
        startActivity(new Intent(this, Support2Activity.class));

    }
}