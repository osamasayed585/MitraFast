package com.linkpcom.mitrafast.MVVM.Client.PaymentCards;

import static com.linkpcom.mitrafast.Classes.Others.MainApplication.creditCardId;
import static com.linkpcom.mitrafast.Classes.Others.MainApplication.value;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.Adapters.PaymentCardsAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ChargeDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeBalanceRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.AddPaymentCard.AddPaymentCardActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityPaymentCardsBinding;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class PaymentCardsActivity extends BaseActivity {

    ActivityPaymentCardsBinding mBinding;
    PaymentCardsViewModel mViewModel;
    ChargeDialog chargeDialog;
    ConfirmDialog confirmDialog;
    private PaymentCardsAdapter paymentCardsAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPaymentCardsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(PaymentCardsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        Intent intent = getIntent();
        if (intent.getData() != null)
            onNewIntent(intent);
        //Request And Observer
        mViewModel.getPaymentCardsResponseMutableLiveData().observe(this, this::onPaymentCardsResponse);
        mViewModel.getChargeResponseMutableLiveData().observe(this, this::onChargeResponse);
        mViewModel.getPaymentResponseMutableLiveData().observe(this, this::onPaymentResponse);
        mViewModel.getDeletePaymentCardResponseMutableLiveData().observe(this, this::onDeleteCard);

        //ViewPager Setup
        paymentCardsAdapter = new PaymentCardsAdapter();
        paymentCardsAdapter.setItemClickListener(this::onPaymentCardClick);
        mBinding.rvPaymentCards.setAdapter(paymentCardsAdapter);

        chargeDialog = new ChargeDialog(this);
        confirmDialog = new ConfirmDialog(this);
        confirmDialog.setConfirmListDialogHandler(this::onClickConfirm);

        mBinding.ivAdd.setOnClickListener(this::onAddClick);

        paymentCardsAdapter.onDeleteCard.subscribe(id -> {

            confirmDialog.setTitle(getResources().getString(R.string.are_you_sure_you_want_to_delete_this_card));
            confirmDialog.setConfirmListDialogHandler(() -> {

                mViewModel.requestDeletePaymentCard(id);
            });

            if (!confirmDialog.getDialog().isShowing())
                confirmDialog.getDialog().show();
        });
    }

    private void onClickConfirm() {

    }

    private void onDeleteCard(BaseResponse baseResponse) {
        if (baseResponse.isSuccess()) {
            mViewModel.requestPaymentCards();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.requestPaymentCards();
    }

    private void onAddClick(View view) {
        startActivity(new Intent(this, AddPaymentCardActivity.class));
    }

    private void onPaymentCardsResponse(PaymentCardsResponse response) {
        paymentCardsAdapter.setData(response.getData());
        if (response.getData().isEmpty())
            mBinding.CurrentCard.setVisibility(View.VISIBLE);

    }

    private void onChargeResponse(BaseResponse response) {
        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        chargeDialog.getDialog().dismiss();
    }

    private void onPaymentCardClick(PaymentCard paymentCard) {
        chargeDialog.setAddCardListDialogHandler((amount) -> {
            chargeDialog.getDialog().dismiss();
            creditCardId = paymentCard.getId();
            value = amount;
            Timber.d("osAndroid " + amount);
            mViewModel.requestChargeBalance(ChargeBalanceRequest.builder()
                    .creditcard_id(paymentCard.getId())
                    .payment_type("creditcard")
                    .redirect_url("app://speedapp/makeWalletCharge")
                    .price(Double.parseDouble(amount))
                    .build());

        });
        chargeDialog.getDialog().show();
    }

    private void onPaymentResponse(PaymentResponse response) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(response.getData().getUrl()));
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Timber.d("Validate Deep Link onNewIntent%s", intent.getData().toString());
        if (Objects.equals(intent.getScheme(), "app")) {
            try {
                String success = intent.getData().getQueryParameter("success");
                if (success.equals("1")) {
                    if (preferencesManager.getCardId() != -1)
                        mViewModel.requestCardValid(preferencesManager.getCardId());
                } else {

                }

            } catch (Exception e) {
                Timber.e(e, "onNewIntent: ");
            }
        }
    }

}