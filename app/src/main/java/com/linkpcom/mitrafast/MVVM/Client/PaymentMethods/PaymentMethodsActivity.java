package com.linkpcom.mitrafast.MVVM.Client.PaymentMethods;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.NAME_CARD;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.linkpcom.mitrafast.Classes.Adapters.PaymentMethodsAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ListDialog;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentMethod;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentMethodsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityPaymentMethodsBinding;

import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PaymentMethodsActivity extends BaseActivity {

    private ActivityPaymentMethodsBinding mBinding;
    PaymentMethodsViewModel mViewModel;
    private PaymentMethodsAdapter paymentMethodsAdapter;
    private double value;
    private ListDialog companiesDialog;
    private List<PaymentCard> paymentCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPaymentMethodsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(PaymentMethodsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        //Request And Observer
        mViewModel.requestPaymentMethods().observe(this, this::onPaymentMethodsResponse);
        mViewModel.requestWallet().observe(this, this::onWalletResponse);

        paymentMethodsAdapter = new PaymentMethodsAdapter();
        paymentMethodsAdapter.setItemClickListener(this::onPaymentMethodClick);
        mBinding.paymentMethodsRecyclerView.setAdapter(paymentMethodsAdapter);

        mBinding.Balance.setOnClickListener(v -> {

            if (value > 0.0) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(CONSTANTS.INTENTS.OBJECT, PaymentMethod.builder().id(5).name(getString(R.string.wallet)).build());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else Snackbar.make(v, R.string.sorry, BaseTransientBottomBar.LENGTH_LONG).show();
        });
    }

    private void onWalletResponse(WalletResponse response) {
        String title = getResources().getString(R.string.use_wallet_balance);
        String wallet = getResources().getString(R.string.use_wallet);
        value = Double.parseDouble(response.getData().getValue());
        String currency = getResources().getString(R.string.Rial);

        if (value > 0.0)
            mBinding.tvBalance.setText(String.format(Locale.US, "%s ( %s %.2f %s )", wallet, title, value, currency));
        else
            mBinding.tvBalance.setText(String.format(Locale.US, "%s ( %s )", wallet, getResources().getString(R.string.noFound)));


    }

    private void onPaymentMethodsResponse(PaymentMethodsResponse response) {
        List<PaymentMethod> paymentMethods = response.getData();
        for (int i = 0; i < paymentMethods.size(); i++) {
            if (paymentMethods.get(i).getId() == 5) {
                paymentMethods.remove(i);
                break;
            }
        }
        paymentMethodsAdapter.setData(paymentMethods);

    }

    private void onPaymentMethodClick(PaymentMethod paymentMethod) {
        Intent returnIntent = new Intent();
        if (paymentMethod.getName().equals("مدى / فيزا")) {

            // call webservers
            mViewModel.requestPaymentCards().observe(this, Response -> {

                ListDialog companies = new ListDialog(this);

                paymentCards = Response.getData();
                companies.setData(Response.getNames());

                companies.getDialog().show();

                companies.setOnItemClickListener(clickedItemPosition -> {

                    PaymentCard selectedCard = paymentCards.get(clickedItemPosition);
                    companies.getDialog().dismiss();

                    returnIntent.putExtra(NAME_CARD, selectedCard);
                    returnIntent.putExtra(OBJECT, paymentMethod);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                });
                companies.otherButton(v -> {
                    companies.getDialog().dismiss();
                    returnIntent.putExtra(OBJECT, paymentMethod);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                });
                companies.setDismissClickListener(view -> companies.getDialog().dismiss());

            });
        } else {
            returnIntent.putExtra(OBJECT, paymentMethod);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

    }

}