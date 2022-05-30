package com.linkpcom.mitrafast.Classes.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.hilt.android.qualifiers.ApplicationContext;
import timber.log.Timber;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.ADDRESS;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.ADDRESS_ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.ADDRESS_NAME;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.AUTHENTICATION_TOKEN;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.AUTHENTICATION_TYPE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.CURRENCY_SYMBOL;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.DELIVERY_SERVICE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.EMAIL;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IMAGE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_AUTH_USER;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_AVAILABLE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_BUSY;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_PROFILE_COMPLETED;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_RESET_PASSWORD;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_SERVICES_COMPLETED;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.IS_USER_VERIFIED;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.LANGUAGE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.LAT;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.LNG;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.MOBILE_PHONE_NUMBER;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.ORDER_ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.ORDER_TYPE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.PHONE_NUMBER_CODE;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.PHONE_NUMBER_COUNT;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.PHONE_START_WITH;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.SECTIONS;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.SHARED_PREFERENCES_NAME;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.USER_ID;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.USER_NAME;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.SHARED_PREFERENCES.USER_TYPE_ID;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class PreferencesManager {

    public static final String CARD_ID = "CARD_ID";
    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferencesManager(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
    }

    public boolean isUserVerified() {
        return sharedPreferences.getBoolean(IS_USER_VERIFIED, false);
    }

    public void setUserVerified(boolean userVerified) {
        sharedPreferences.edit().putBoolean(IS_USER_VERIFIED, userVerified).apply();
    }

    public boolean isProfileComplete() {
        return sharedPreferences.getBoolean(IS_PROFILE_COMPLETED, false);
    }

    public void setProfileComplete(boolean isProfileComplete) {
        sharedPreferences.edit().putBoolean(IS_PROFILE_COMPLETED, isProfileComplete).apply();

    }

    public boolean isResetPassword() {
        return sharedPreferences.getBoolean(IS_RESET_PASSWORD, false);
    }

    public void setResetPassword(boolean isResetPassword) {
        sharedPreferences.edit().putBoolean(IS_RESET_PASSWORD, isResetPassword).apply();

    }

    public String getAuthenticationToken() {
        return sharedPreferences.getString(AUTHENTICATION_TOKEN, null);
    }

    public void setDeliveryService(boolean isOpen) {
        sharedPreferences.edit().putBoolean(DELIVERY_SERVICE, isOpen).apply();

    }

    public boolean getDeliveryService() {
        return sharedPreferences.getBoolean(DELIVERY_SERVICE, false);
    }

    public void setAuthenticationToken(String authenticationToken) {
        sharedPreferences.edit().putString(AUTHENTICATION_TOKEN, authenticationToken).apply();

    }

    public String getAuthenticationType() {
        return sharedPreferences.getString(AUTHENTICATION_TYPE, null);
    }

    public void setAuthenticationType(String authenticationType) {
        sharedPreferences.edit().putString(AUTHENTICATION_TYPE, authenticationType).apply();

    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void setUserId(String userId) {
        sharedPreferences.edit().putString(USER_ID, userId).apply();
    }

    public String getName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(USER_NAME, name).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(EMAIL, email).apply();

    }

    public String getImage() {
        return sharedPreferences.getString(IMAGE, null);
    }

    public void setImage(String image) {
        sharedPreferences.edit().putString(IMAGE, image).apply();

    }

    public String getCurrencySymbol() {
        return sharedPreferences.getString(CURRENCY_SYMBOL, null);
    }

    public void setCurrencySymbol(String currencySymbol) {
        sharedPreferences.edit().putString(CURRENCY_SYMBOL, currencySymbol).apply();

    }

    public String getLanguage() {
        return sharedPreferences.getString(LANGUAGE, "ar");
    }

    public void setLanguage(String language) {
        sharedPreferences.edit().putString(LANGUAGE, language).apply();

    }

    // "" To Prevent Null
    public String getUserTypeId() {
        return sharedPreferences.getString(USER_TYPE_ID, "");
    }

    public void setUserTypeId(String userTypeId) {
        sharedPreferences.edit().putString(USER_TYPE_ID, userTypeId).apply();

    }

    public String getOrderId() {
        return sharedPreferences.getString(ORDER_ID, "");
    }

    public void setOrderId(String orderId) {
        sharedPreferences.edit().putString(ORDER_ID, orderId).apply();

    }

    public void setLat(String latitude) {
        sharedPreferences.edit().putString(LAT, latitude).apply();
    }

    public String getLat() {
        return sharedPreferences.getString(LAT, null);
    }

    public void setLng(String longitude) {
        sharedPreferences.edit().putString(LNG, longitude).apply();
    }

    public String getLng() {
        return sharedPreferences.getString(LNG, null);
    }

    public void setAddress(String address) {
        sharedPreferences.edit().putString(ADDRESS, address).apply();
    }

    public String getAddress() {
        return sharedPreferences.getString(ADDRESS, null);
    }

    public void setAddressName(String addressName) {
        sharedPreferences.edit().putString(ADDRESS_NAME, addressName).apply();
    }

    public String getAddressName() {
        return sharedPreferences.getString(ADDRESS_NAME, null);
    }

    public void setAddressId(String addressId) {
        sharedPreferences.edit().putString(ADDRESS_ID, addressId).apply();
    }

    public String getAddressId() {
        return sharedPreferences.getString(ADDRESS_ID, null);
    }

    public boolean isAvailable() {
        return sharedPreferences.getBoolean(IS_AVAILABLE, false);
    }

    public void setAvailable(boolean isAvailable) {
        sharedPreferences.edit().putBoolean(IS_AVAILABLE, isAvailable).apply();

    }

    public boolean isBusy() {
        return sharedPreferences.getBoolean(IS_BUSY, false);
    }

    public void setBusy(boolean isBusy) {
        sharedPreferences.edit().putBoolean(IS_BUSY, isBusy).apply();

    }

    public void removeUser() {
        Timber.d("Shady removeUser: ");
        sharedPreferences.edit().putString(AUTHENTICATION_TOKEN, null)
                .putString(AUTHENTICATION_TYPE, null)
                .putString(MOBILE_PHONE_NUMBER, null)
                .putString(PHONE_START_WITH, null)
                .putString(PHONE_NUMBER_COUNT, null)
                .putString(PHONE_NUMBER_CODE, null)
                .putString(USER_NAME, null)
                .putString(EMAIL, null)
                .putString(IMAGE, null)
                .putString(CURRENCY_SYMBOL, null)
                .putString(ORDER_ID, null)
                .putString(ORDER_TYPE, null)
                .putString(USER_TYPE_ID, null)
                .putBoolean(IS_PROFILE_COMPLETED, false)
                .putBoolean(IS_SERVICES_COMPLETED, false)
                .putBoolean(IS_USER_VERIFIED, false)
                .putBoolean(IS_AVAILABLE, false)
                .putBoolean(IS_BUSY, false)
                .putBoolean(IS_RESET_PASSWORD, false)
                .putString(SECTIONS, null)
                .apply();


        sharedPreferences.edit().remove(SECTIONS).apply();
    }

    public void setCardId(int card_id) {
        sharedPreferences.edit().putInt(CARD_ID, card_id).apply();
    }

    public int getCardId() {
        return sharedPreferences.getInt(CARD_ID, -1);
    }
}