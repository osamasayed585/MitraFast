package com.linkpcom.mitrafast.Classes.Utils;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * when be date as a long > how to convert to normal date
     */
    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy  h:mm aa", Locale.ENGLISH);
        return format.format(date);
    }


    /**
     * Returns the {@code location} object as a human readable string.
     *
     * @param location The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }


    /**
     * for change distance form arabic to english and vice versa
     */
    public static String convertKmToArabic(Context context, String str) {
        String language = context.getResources().getConfiguration().locale.getLanguage();
        if (language.equals("ar")) {

            if (str.contains("km")) {
                String base = str.substring(0, str.length() - 2);
                return String.format("%s %s", base, "كم");
            } else return str;

        } else return str;
    }

}
