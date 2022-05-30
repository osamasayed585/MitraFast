package com.linkpcom.mitrafast.Classes.Utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import timber.log.Timber;

public class OrderLiveStatus {
    public interface StatusChanged {
        void onStatusChanged();
    }

    private StatusChanged statusChanged;
    private DatabaseReference orderStatusRef;
    private boolean initial = true;


    public OrderLiveStatus(int orderId) {
        if (orderStatusRef != null && orderStatusRef.getRef().equals(FireBaseReferences.getOrderStatusRef(String.valueOf(orderId))))
            return;
        orderStatusRef = FireBaseReferences.getOrderStatusRef(String.valueOf(orderId));
        orderStatusRef.addValueEventListener(orderStatusListener);
    }

    public void setStatusChanged(StatusChanged statusChanged) {
        this.statusChanged = statusChanged;
    }

    ValueEventListener orderStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getRef().equals(orderStatusRef))
                if (dataSnapshot.exists())
                    if (dataSnapshot.child(FireBaseDB.status).exists()) {
                        if (initial)
                            initial = false;
                        else
                            statusChanged.onStatusChanged();
                    }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());

        }
    };

}
