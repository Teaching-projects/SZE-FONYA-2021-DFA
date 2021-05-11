package com.myitsolver.baseandroidapp.logic.appstate

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.myitsolver.baseandroidapp.util.ifNotNull
import com.snakydesign.livedataextensions.distinctUntilChanged
import com.snakydesign.livedataextensions.nonNull


class AppState(applicationContext: Context) {
    private val _networkEnabled = MutableLiveData<Boolean>()
    val networkEnabled = _networkEnabled.distinctUntilChanged()
    private val _locationEnabled = GpsStatusListener(applicationContext)
    val locationEnabled = _locationEnabled.distinctUntilChanged()
    private val _locationPermissionGranted = MutableLiveData<Boolean>()
    val locationPermissionGranted = _locationPermissionGranted.distinctUntilChanged()

    val locationActive: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        var loc: Boolean? = null
        var perm: Boolean? = null
        addSource(_locationEnabled) {
            loc = it is GpsStatus.Enabled
            postValue( loc != false && perm != false)
        }
        addSource(_locationPermissionGranted){
            perm = it
            postValue( loc != false && perm != false)
        }
    }


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation = _currentLocation.nonNull()

    fun setNetworkAvailability(available: Boolean) {
        _networkEnabled.postValue(available)
    }

    fun setLocationPermissionGranted(granted: Boolean) {
        _locationPermissionGranted.postValue(granted)
    }

    fun setLocationEnabled(enabled: Boolean) {
//        _locationEnabled.postValue(enabled)
    }

    fun setCurrentLocation(lat: Double?, lon: Double?) {
        _currentLocation.postValue(ifNotNull(lat, lon) { _lat, _lon -> LatLng(_lat, _lon) })
    }


}