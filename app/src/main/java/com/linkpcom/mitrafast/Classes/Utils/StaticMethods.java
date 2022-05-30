package com.linkpcom.mitrafast.Classes.Utils;

import static java.lang.Math.round;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

//import com.google.android.gms.maps.model.LatLng;

public class StaticMethods {


    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

    }


//    public static float distance(LatLng firstLocation, LatLng secondLocation) {
//        Location firstLoc = new Location("service Provider");
//        Location secondLoc = new Location("service Provider");
//
//        firstLoc.setLatitude(firstLocation.latitude);
//        firstLoc.setLongitude(firstLocation.longitude);
//        secondLoc.setLatitude(secondLocation.latitude);
//        secondLoc.setLongitude(secondLocation.longitude);
//        return firstLoc.distanceTo(secondLoc) / 1000;
//    }

    public static float distance(LatLng firstLocation, LatLng secondLocation) {
        Location firstLoc = new Location("service Provider");
        Location secondLoc = new Location("service Provider");

        firstLoc.setLatitude(firstLocation.latitude);
        firstLoc.setLongitude(firstLocation.longitude);
        secondLoc.setLatitude(secondLocation.latitude);
        secondLoc.setLongitude(secondLocation.longitude);
        return firstLoc.distanceTo(secondLoc) / 1000;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double distance = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = distance * 60 * 1.1515;

        distance = round(distance);
        return (distance);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 500;
        int maxHeight = 500;


        if (maxWidth >= width && maxHeight >= height)
            return bm;
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();


        if (maxWidth >= width && maxHeight >= height)
            return bm;
        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static MultipartBody.Part getFileRequestBody(File file, String name) {

        MultipartBody.Part part = null;
        if (file != null) {
            final RequestBody request_body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            part = MultipartBody.Part.createFormData(name, file.getName(), request_body);
        }
        return part;
    }


    public static MultipartBody.Part[] getImagesRequestBody(List<Image> images) {
        for (Image filePath : images) {
            File file = new File(filePath.getPath());

        }
        MultipartBody.Part[] parts = new MultipartBody.Part[images.size()];
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            parts[i] = getImageRequestBody(image.getPath());

        }
        return parts;
    }

    public static MultipartBody.Part getImageRequestBody(String image) {

        MultipartBody.Part part = null;
        if (image != null) {
            if (!image.equals("null")) {
                File f = new File(image);
                final RequestBody request_body = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                part = MultipartBody.Part.createFormData("images[]", f.getName(), request_body);
            }

        }
        return part;
    }

    public static MultipartBody.Part getImageRequestBody(String image, String name) {

        MultipartBody.Part part = null;
        if (image != null) {
            if (!image.equals("null")) {
                File f = new File(image);
                final RequestBody request_body = RequestBody.create(MediaType.parse("multipart/form-data"), f);
                part = MultipartBody.Part.createFormData(name, f.getName(), request_body);
            }

        }
        return part;
    }
}
