package com.myitsolver.baseandroidapp.util.maps;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;

import java.text.DecimalFormat;

/**
 * Created by Peter on 2017. 10. 01..
 */

public class LastKnownLocationProvider {
    public static void getLatestLocation(final Activity context, final OnLocationArrivedListener listener) {

        Dexter.withActivity(context)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new BasePermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(context, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (listener != null) {
                                    listener.onArrived(location);
                                }
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        listener.onArrived(null);
                    }
                })
                .check();
    }

    public static double getDistanceInMeters(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lng2 - lng1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

    public static String getFormattedDistance(double lat1, double lng1, double lat2, double lng2) {
        double meters = getDistanceInMeters(lat1, lng1, lat2, lng2);

        if (meters < 1000) {
            DecimalFormat df = new DecimalFormat("#");
            return df.format(meters) + " m";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(meters / 1000) + " km";
        }
    }

    public interface OnLocationArrivedListener {
        void onArrived(@Nullable Location location);
    }
}
