package com.linkpcom.mitrafast.MVVM.Agent.OrderDetails;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.IS_WAITING;

import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptedForOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FirebaseOrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.Utils.MarkersGenerator;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder.InProgressOrderActivity;
import com.linkpcom.mitrafast.MVVM.Agent.Main.MainActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityAgentOrderDetailsBinding;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class OrderDetailsActivity extends BaseActivity implements OnMapReadyCallback {


    public static final int ORDER_NOT_SET = -1;
    private ActivityAgentOrderDetailsBinding mBinding;
    private OrderDetailsViewModel mViewModel;


    int orderId;
    Order mOrder;
    boolean isWaiting;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    MarkersGenerator markersGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAgentOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        orderId = getIntent().getIntExtra(ID, ORDER_NOT_SET);
        isWaiting = getIntent().getBooleanExtra(IS_WAITING, false);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mViewModel.getOrderDetailsResponseMutableLiveData().observe(this, this::onOrderDetailsResponse);
        mViewModel.getFirebaseOrderDetailsResponseMutableLiveData().observe(this, this::onFirebaseOrderDetailsResponse);
        mViewModel.getAgentAcceptOrderResponseMutableLiveData().observe(this, this::onAgentAcceptOrderResponse);
        mViewModel.getAcceptedForOrderMutableLiveData().observe(this, this::onAcceptedForOrderResponse);
        mViewModel.getIgnoreOrderResponseMutableLiveData().observe(this, this::onIgnoreOrderResponse);
        mViewModel.getCurrentLocationResponseMutableLiveData().observe(this, this::onLocationResponseHandler);

        initMap();

        mBinding.btnAccept.setOnClickListener(this::onAcceptClick);
        mBinding.btnIgnore.setOnClickListener(this::onIgnoreClick);
        markersGenerator = new MarkersGenerator(this);
        mViewModel.startOrderDetailsListening(String.valueOf(orderId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.requestOrderDetails(orderId);

    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onLocationResponseHandler(Location location) {
        LatLng driverLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng pickupLatLng = new LatLng(Double.parseDouble(mOrder.getShop().getLat()), Double.parseDouble(mOrder.getShop().getLng()));
        LatLng deliveryLatLng = new LatLng(Double.parseDouble(mOrder.getLocation().getLat()), Double.parseDouble(mOrder.getLocation().getLng()));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(driverLatLng);
        builder.include(pickupLatLng);
        builder.include(deliveryLatLng);
        LatLngBounds bounds = builder.build();
        int padding = 256;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cameraUpdate);

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markersGenerator.getViewDriver())).position(driverLatLng));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickuo_pin)).position(pickupLatLng));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_drop_off_pin)).position(deliveryLatLng));


        enableMapLocation();
    }
    @SuppressLint("MissingPermission")
    private void enableMapLocation() {
        mMap.setMyLocationEnabled(true);

        ImageView locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if (locationButton != null) {
            locationButton.setBackgroundResource(R.drawable.circle);
            locationButton.setImageResource(R.drawable.ic_current_location);
            locationButton.setPadding(24, 24, 24, 24);
            ((ViewGroup) locationButton.getParent()).removeView(locationButton);
            mBinding.locationButtonContainer.addView(locationButton);
        }
    }


    private void onIgnoreOrderResponse(BaseResponse response) {
        finish();
    }

    public void onIgnoreClick(View view) {
        mViewModel.requestIgnoreOrder(orderId);
    }

    public void onAcceptClick(View view) {
        mViewModel.requestAgentAcceptOrder(orderId);
    }

    private void onAgentAcceptOrderResponse(BaseResponse response) {
        mViewModel.startOrderListening(String.valueOf(orderId));
        isWaiting = true;
        mBinding.llMakeOfferContainer.setVisibility(View.GONE);
        mBinding.llWaitClientContainer.setVisibility(View.VISIBLE);
    }

    private void onAcceptedForOrderResponse(AcceptedForOrderRequest request) {
        Timber.d("Shady onAcceptedForOrderResponse: %s", request.getOrder_status());
        if (request.getOrder_status() != null) {
            //16 Constant For Status Of Client Accepted An Agent
            if (request.getOrder_status().equals("16")) {
                if (request.is_agent_exist()) {
                    preferencesManager.setBusy(true);
                    mViewModel.requestRemoveAvailableLocation();


                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    taskStackBuilder.addNextIntentWithParentStack(mainIntent);
                    Intent intent = new Intent(this, InProgressOrderActivity.class);
                    intent.putExtra(CONSTANTS.INTENTS.ID, orderId);
                    taskStackBuilder.addNextIntent(intent);
                    taskStackBuilder.startActivities();

                } else {
                    finish();
                }
            } else {
                if (!request.is_agent_exist()) {
                    finish();
                }
            }
        } else {
            finish();
        }
    }

    private void onOrderDetailsResponse(OrderDetailsResponse response) {
        mOrder = response.getData();

        mBinding.tvTitle.setText(String.format("#%s", mOrder.getId()));
        mBinding.tvOrderNumber.setText(String.valueOf(mOrder.getId()));
        mBinding.tvClientAddress.setText(String.format("%s %s",getResources().getString(R.string.ClientAddress),mOrder.getLocation().getAddress()));
        mBinding.tvShopAddress.setText(String.format("%s %s",getResources().getString(R.string.ShopAddress),mOrder.getShop().getShop_address()));
        mBinding.textView7.setText(String.format("%s %s %s", getString(R.string.delivery_cost),mOrder.getDelivery_cost(), mOrder.getCurrency()));

        if (isWaiting) {
            mBinding.llMakeOfferContainer.setVisibility(View.GONE);
            mBinding.llWaitClientContainer.setVisibility(View.VISIBLE);
            mViewModel.removeOrderListener();
            mViewModel.startOrderListening(String.valueOf(orderId));
        }
        mViewModel.requestCurrentLocation(this);

    }

    private void onFirebaseOrderDetailsResponse(FirebaseOrderDetailsResponse response) {
        mBinding.tvDistanceShopToClient.setText(response.getDistance_text());
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

}