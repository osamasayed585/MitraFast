package com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder;

import static android.view.View.GONE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.DATE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.linkpcom.mitrafast.Classes.Adapters.OrderContentsAdapter;
import com.linkpcom.mitrafast.Classes.Adapters.OrderStatusesAdapter;
import com.linkpcom.mitrafast.Classes.Dialogs.ConfirmDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.DeleteOrderListDialog;
import com.linkpcom.mitrafast.Classes.Dialogs.OrderStatusesDialog;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CancelReason;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.UpdateOrderStatusRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CancelOrderReasonsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.StatusesResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.RateClient.RateClientActivity;
import com.linkpcom.mitrafast.MVVM.Chat.ChatActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityInProgressOrderBinding;
import com.squareup.picasso.Picasso;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

@AndroidEntryPoint
public class InProgressOrderActivity extends BaseActivity
        implements OrderStatusesAdapter.OrderStatusesAdapterOnClickHandler,
        OrderStatusesAdapter.OnStatusChangeListener {

    public static final int DEFAULT_VALUE = -1;
    private int orderId;
    private int nextCase = 0;
    private ActivityInProgressOrderBinding mBinding;
    private InProgressOrderViewModel mViewModel;
    private Order mOrder;
    private OrderContentsAdapter orderContentsAdapter;
    private DeleteOrderListDialog deleteOrderListDialog;
    private List<CancelReason> cancelReasons;
    private CancelReason selectedCancelOrder;
    private ConfirmDialog confirmDialog;
    private OrderStatusesDialog orderStatusesDialog;
    private OrderStatusesAdapter orderStatusesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityInProgressOrderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getIntExtra(ID, DEFAULT_VALUE);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(InProgressOrderViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getOrderDetailsResponseMutableLiveData().observe(this, this::onOrderDetailsResponse);
        mViewModel.getCancelOrderReasonsResponseMutableLiveData().observe(this, this::onCancelOrderReasonsResponse);
        mViewModel.getCancelOrderResponseMutableLiveData().observe(this, this::onCancelOrderResponse);
        mViewModel.getFinishOrderResponseMutableLiveData().observe(this, this::onFinishOrderResponse);
        mViewModel.getUpdateOrderStatusResponseMutableLiveData().observe(this, this::onUpdateOrderStatusResponse);
        mViewModel.getOrderStatusMutableLiveData().observe(this, this::onOrderStatusResponse);


        //New Instances
        orderContentsAdapter = new OrderContentsAdapter();
        confirmDialog = new ConfirmDialog(this);
        orderStatusesDialog = new OrderStatusesDialog(this);
        deleteOrderListDialog = new DeleteOrderListDialog(this);
        cancelReasons = new ArrayList<>();

        mBinding.orderContents.setAdapter(orderContentsAdapter);


        mBinding.directions.setOnClickListener(__ -> onDirectionsClick(mOrder.getLocation().getLat(), mOrder.getLocation().getLng()));
        mBinding.directionsShop.setOnClickListener(__ -> onDirectionsClick(mOrder.getShop().getLat(), mOrder.getShop().getLng()));
        mBinding.cancelOrder.setOnClickListener(this::onCancelOrderClick);
        mBinding.orderDelivered.setOnClickListener(this::onOrderDeliveredClick);
        // mBinding.changeOrderStatus.setOnClickListener(this::onChangeOrderStatusClick);

        // step one hit for api
        mViewModel.requestOrderStatuses(orderId).observe(this, this::onStatusResponse);

        mViewModel.requestOrderDetails(orderId);
        mViewModel.startOrderStatusListening(orderId);

        mBinding.CommunicateCustomer.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(OBJECT, mOrder);
            startActivity(intent);
        });

        mBinding.call.setOnClickListener(__ -> startCall(mOrder.getUser().getMobile()));
        mBinding.callShop.setOnClickListener(__ -> startCall(mOrder.getShop().getMobile()));

    }

    private void startCall(String mobile) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + "+966" + mobile));
            startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onStatusResponse(StatusesResponse statusesResponse) {
        orderStatusesAdapter = new OrderStatusesAdapter();
        orderStatusesAdapter.setData(statusesResponse.getData());

        mBinding.changeOrderStatus.setLayoutManager(new LinearLayoutManager(this));
        mBinding.changeOrderStatus.setAdapter(orderStatusesAdapter);

        orderStatusesAdapter.initialItemClickListener(this);
        orderStatusesAdapter.initStatusAgent(this);

        mBinding.changeOrderStatus.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
            }

            @Override
            public void onSwipedRight(int position) { // 0
                ++nextCase;
                orderStatusesAdapter.setSelectedItemPosition(nextCase); // 1

            }
        });
    }

    private void onUpdateOrderStatusResponse(BaseResponse response) {
        mViewModel.requestOrderDetails(orderId);
    }

    private void onFinishOrderResponse(BaseResponse response) {
    }

    private void onOrderDeliveredClick(View view) {
        confirmDialog.setConfirmListDialogHandler(() -> {
            mViewModel.requestFinishOrder(orderId);
        });
        confirmDialog.getDialog().show();
    }

    private void onCancelOrderClick(View view) {
        mViewModel.requestCancelOrderReasons();
    }

    private void onDirectionsClick(String lat, String lng) {
        try {
            Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_map_ap), Toast.LENGTH_SHORT).show();
        }
    }

    private void onWhatsAppClick(View view) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(mOrder.getUser().getMobile()) + "@s.whatsapp.net");
            startActivity(sendIntent);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void onOrderDetailsResponse(OrderDetailsResponse response) {
        mOrder = response.getData();
        mBinding.orderId.setText(String.valueOf(mOrder.getId()));
        mBinding.clientName.setText(mOrder.getUser().getName());
        Picasso.get().load(mOrder.getUser().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.clientImage);
        mBinding.clientRate.setStar((int) mOrder.getUser().getRate());
        mBinding.orderDate.setText(mOrder.getDate());
        Picasso.get().load(mOrder.getShop().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(mBinding.shopImage);
        mBinding.shopName.setText(mOrder.getShop().getName());
        orderContentsAdapter.setData(mOrder.getProducts());
        mBinding.clientLocation.setText(mOrder.getLocation().getAddress());
        double cost = Double.parseDouble(mOrder.getDelivery_cost_with_tax());
        mBinding.deliverCost.setText(String.format(Locale.ENGLISH, "%s %.2f %s", getString(R.string.deliver_cost), cost, mOrder.getCurrency()));

        if (mOrder.getNotes() != null) {
            mBinding.orderNote.setText(mOrder.getNotes());
            mBinding.orderNoteLabel.setVisibility(View.VISIBLE);
        } else
            mBinding.orderNoteLabel.setVisibility(GONE);
    }

    private void onCancelOrderReasonsResponse(CancelOrderReasonsResponse response) {
        cancelReasons = response.getData();

        deleteOrderListDialog.setData(cancelReasons);
        deleteOrderListDialog.setDeleteOrderListDialogHandler(selectedCancelOrder -> {
            this.selectedCancelOrder = selectedCancelOrder;
            mViewModel.requestCancelOrder(CancelOrderRequest.builder()
                    .order_id(String.valueOf(mOrder.getId()))
                    .cancellation_reason_id(String.valueOf(selectedCancelOrder.getId()))
                    .build());
        });
        deleteOrderListDialog.getDialog().show();
    }

    private void onCancelOrderResponse(BaseResponse response) {
        preferencesManager.setBusy(false);
        deleteOrderListDialog.getDialog().dismiss();
        finish();
    }

    private void onOrderStatusResponse(String orderStatus) {
        // 7 driverCompleteOrder
        if (orderStatus.equals("7")) {
            mViewModel.removeOrderStatusListener();
            preferencesManager.setBusy(false);
            mViewModel.requestRemoveBusyLocation();
            Intent intent = new Intent(this, RateClientActivity.class);
            intent.putExtra(ID, orderId);
            intent.putExtra(DATE, mOrder.getDate());
            finish();
            startActivity(intent);
        }else{
            mViewModel.requestOrderStatuses(orderId).observe(this, this::onStatusResponse);
        }
    }

    @Override
    public void isStatusFinished(boolean status) {
        // mOrder.getOrder_status().getId() == doesDriverArrive &&
        if (status) {
            mBinding.changeOrderStatus.setVisibility(GONE);
            mBinding.orderDelivered.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isStatusChange(int orderStatusId) {
        mViewModel.requestUpdateOrderStatus(UpdateOrderStatusRequest.builder()
                .order_id(mOrder.getId())
                .order_status_id(orderStatusId)
                .build());
    }
}