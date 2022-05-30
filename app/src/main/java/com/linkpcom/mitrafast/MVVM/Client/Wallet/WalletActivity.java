package com.linkpcom.mitrafast.MVVM.Client.Wallet;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.linkpcom.mitrafast.Classes.Adapters.TransactionsAdapter;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Transaction;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TransactionsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.PaymentCards.PaymentCardsActivity;
import com.linkpcom.mitrafast.databinding.ActivityWalletBinding;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WalletActivity extends BaseActivity {

    private ActivityWalletBinding mBinding;
    private WalletViewModel mViewModel;
    private TransactionsAdapter transactionsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getTransactionsResponseMutableLiveData().observe(this, this::onTransactionsResponse);
        mViewModel.getWalletResponseMutableLiveData().observe(this, this::onWalletResponse);

        //New Instances
        transactionsAdapter = new TransactionsAdapter();

        transactionsAdapter.setItemClickListener(this::onTransactionClick);

        mBinding.rvTransactions.setAdapter(transactionsAdapter);
        mBinding.ivAddBalance.setOnClickListener(this::onAddBalanceClick);

    }

    private void onWalletResponse(WalletResponse response) {
        mBinding.tvBalance.setText(String.format(Locale.ENGLISH, "%.2f %s",
                Double.parseDouble(response.getData().getValue()), response.getData().getCurrency()));
        transactionsAdapter.setCurrency(response.getData().getCurrency());
    }

    private void onAddBalanceClick(View view) {
        startActivity(new Intent(this, PaymentCardsActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestTransactions();
        mViewModel.requestWallet();
    }

    private void onTransactionClick(Transaction transaction) {
    }

    private void onTransactionsResponse(TransactionsResponse response) {
        transactionsAdapter.setData(response.getData());
    }
}
