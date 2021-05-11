package com.myitsolver.baseandroidapp.util.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Peter on 2017. 10. 01..
 */

@SuppressWarnings("unchecked")
public abstract class MapMarkerManager<T extends MarkerProvider> implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback{
    private GoogleMap map;
    private OnItemSelectedListener<T> listener;
    private Activity context;
    private OnMapReadyCallback callback;

    public MapMarkerManager(Fragment fragment, int mapResId, OnItemSelectedListener<T> listener, OnMapReadyCallback onMapReadyCallback) {
        this.listener = listener;
        this.callback = onMapReadyCallback;
        this.context = fragment.getActivity();

        SupportMapFragment mapFragment = (SupportMapFragment) fragment.getChildFragmentManager()
                .findFragmentById(mapResId);
        mapFragment.getMapAsync(this);


    }

    public void setData(T data){
        List<T> list = new ArrayList<>();
        list.add(data);
        setData(list);
    }

    public void setData(List<T> data){
        if (map == null)return;

        map.clear();
        for (T r : data){

// TODO           Marker m = map.addMarker(new MarkerOptions()
//                    .position(new LatLng(r.getLocation().getLat(),r.getLocation().getLng()))
//                    .title(r.getTitle()));
//            m.setTag(r);
//            m.setIcon(BitmapDescriptorFactory.fromResource(getMarkerIcon(m,r)));
        }
    }

    protected abstract int getMarkerIcon(Marker m, T data);

    @Override
    public boolean onMarkerClick(Marker marker) {
        T r = (T) marker.getTag();
        if (r == null){
            return false;
        }
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(r.getLocation().getLat(),r.getLocation().getLng()),15)
        );

        if (listener != null){
            listener.onItemSelected(r);
        }
        return true;
    }

    public void centerMyLocation(){
        Dexter.withActivity(context)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new BasePermissionListener(){
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        MapMarkerManager.this.map.setMyLocationEnabled(true);

                        LastKnownLocationProvider.getLatestLocation(context, new LastKnownLocationProvider.OnLocationArrivedListener() {

                            @Override
                            public void onArrived(@Nullable android.location.Location location) {
                                if (location != null) {
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),13));
                                }else{
                                    centerHungary();
                                }
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        centerHungary();
                    }
                }).check();

    }

    public void centerHungary(){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.17,19.29),8));
    }

    public void centerCustomLocation(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLat(),location.getLng()),14));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (this.map != null){
            map.setOnMarkerClickListener(this);
        }

        if (callback != null){
            callback.onMapReady(googleMap);
        }
    }
    public interface  OnItemSelectedListener<T>{
        void onItemSelected(T item);
    }
}
