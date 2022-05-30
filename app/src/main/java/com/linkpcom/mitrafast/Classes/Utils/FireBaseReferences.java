package com.linkpcom.mitrafast.Classes.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseReferences {
    final static DatabaseReference drivers = FirebaseDatabase.getInstance().getReference(FireBaseDB.drivers);
    final static DatabaseReference Users = FirebaseDatabase.getInstance().getReference(FireBaseDB.Users);
    final static DatabaseReference Orders = FirebaseDatabase.getInstance().getReference(FireBaseDB.Orders);


    public static DatabaseReference getAvailableRef() {
        return drivers.child(FireBaseDB.available);
    }

    public static DatabaseReference getOfflineRef() {
        return drivers.child(FireBaseDB.offline);
    }

    public static DatabaseReference getBusyRef() {
        return drivers.child(FireBaseDB.busy);
    }

    public static DatabaseReference getAgentOrdersRef(String agentId) {
        return Users.child(FireBaseDB.Providers).child(agentId).child(FireBaseDB.orders);
    }

//    public static DatabaseReference getAgentRef(String agentId) {
//        return Users.child(FireBaseDB.Providers).child(agentId);
//    }

    public static DatabaseReference getClientOrderRef(String orderId) {
        return Orders.child(orderId);
    }

    public static DatabaseReference getAgentsRef(String orderId) {
        return Orders.child(orderId).child(FireBaseDB.drivers);
    }

    public static DatabaseReference getOrderStatusRef(String orderId) {
        return Orders.child(orderId);
    }
    public static DatabaseReference getOrderDetailsRef(String orderId) {
        return Orders.child(orderId).child(FireBaseDB.order_details);
    }

    public static DatabaseReference getDriverLocationRef(String driverId) {
        return drivers.child(FireBaseDB.busy).child(driverId);
    }

    public static DatabaseReference getChatRef(String consultationId) {
        return Orders.child(consultationId).child(FireBaseDB.messages);
    }
}
