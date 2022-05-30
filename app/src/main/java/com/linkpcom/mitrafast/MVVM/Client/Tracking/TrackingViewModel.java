package com.linkpcom.mitrafast.MVVM.Client.Tracking;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;
import timber.log.Timber;


@Getter
@HiltViewModel
public class TrackingViewModel extends BaseViewModel {


    @Inject
    public TrackingViewModel() {
    }


    @Inject
    MutableLiveData<LatLng> driverLocationMutableLiveData;

    private DatabaseReference driverLocationRef;

    ValueEventListener driverLocationListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists())
                driverLocationMutableLiveData.setValue(new LatLng(dataSnapshot.child("l").child("0").getValue(Double.class), dataSnapshot.child("l").child("1").getValue(Double.class)));

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());
        }
    };

    public void startDriverLocationListening(String driverId) {
        if (driverLocationRef != null)
            return;

        driverLocationRef = FireBaseReferences.getDriverLocationRef(driverId);
        driverLocationRef.addValueEventListener(driverLocationListener);

    }

    public void removeDriverLocationListener() {
        if (driverLocationRef != null)
            driverLocationRef.removeEventListener(driverLocationListener);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeDriverLocationListener();
    }
}