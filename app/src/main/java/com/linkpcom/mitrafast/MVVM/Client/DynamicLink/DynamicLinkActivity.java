package com.linkpcom.mitrafast.MVVM.Client.DynamicLink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.MVVM.Client.ProductDetails.ProductDetailsActivity;
import com.linkpcom.mitrafast.MVVM.Client.ShopDetails.ShopDetailsActivity;
import com.linkpcom.mitrafast.R;

import timber.log.Timber;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.PRODUCT_ID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DynamicLinkActivity extends BaseActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_link);
        Timber.d("Karim Dynamic key: %s", "start");
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::validation, 2800);
    }

    private void validation() {
        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        for (String key : extras.keySet()) {
            Timber.d("Karim Dynamic key: %s", key);
//                Log.d ("myApplication", key + " is a key in the bundle");
        }
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, this::onSuccess)
                .addOnFailureListener(this, e -> Timber.w(e, "getDynamicLink:onFailure"));
    }

    private void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
        // Get deep link from result (may be null if no link is found)
        Timber.d("Osama called");
        Uri deepLink = null;
        if (pendingDynamicLinkData != null) {
            deepLink = pendingDynamicLinkData.getLink();
        }

        if (deepLink != null) {
            if (deepLink.getQueryParameter("type").equals("shop")) {
                Intent toShopDetails = new Intent(DynamicLinkActivity.this, ShopDetailsActivity.class);
                toShopDetails.putExtra(ID, Integer.parseInt(deepLink.getQueryParameter("id")));
                startActivity(toShopDetails);
            } else {
                Intent toProductDetails = new Intent(DynamicLinkActivity.this, ProductDetailsActivity.class);
                toProductDetails.putExtra(PRODUCT_ID, Integer.parseInt(deepLink.getQueryParameter("id")));
                startActivity(toProductDetails);
            }
            finish();
        } else {
            Timber.d("getDynamicLink: no link found");
        }
    }


}