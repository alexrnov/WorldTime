package alexrnov.worldtime.ui.sun

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.common.ConnectionResult.*
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class LocationObserver(
        private val fragment: Fragment,
        private val lifecycle: Lifecycle) : LifecycleObserver {

    private var locationClient: FusedLocationProviderClient? = null
    private val locationListener = OnSuccessListener { location: Location? ->
        // Got last known location. In some rare situations this can be null.
        if (location != null) {
            // Logic to handle location object
            Log.i("P", "lat: " + location.latitude + ", "
                    + "long: " + location.longitude)
        } else {
            Log.i("P", "location is null")
        }
    }

    // because permission was granted, we can set annotation "MissingPermission"
    @SuppressLint("MissingPermission")
    fun getLocation() {
        // check for a specific state. Since multiple states can interleave for a given point
        // of time, if we want to check for a specific state, we always use the isAtLeast method
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            locationClient?.lastLocation?.addOnSuccessListener(fragment.requireActivity(), locationListener)
            Log.i("P", "location started")
        } else {
            Log.i("P", "location not started")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        locationClient = LocationServices.getFusedLocationProviderClient(fragment.requireActivity())
        // required before get location
        if (ActivityCompat.checkSelfPermission(fragment.requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // required for Android M (SDK API 23+)
                //ActivityCompat.requestPermissions( // if invoked from activity
                       // activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }
        } else { // when permission is present (on repeated calls)
            locationClient?.lastLocation?.addOnSuccessListener(fragment.requireActivity(), locationListener)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.i("P", "stop lifecycle")
        locationClient = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        Log.i("P", "resume lifecycle")
        when (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(fragment.requireContext())) {
            SUCCESS -> Log.i("P", "googleService success")
            SERVICE_MISSING -> Log.i("P", "googleService missing")
            SERVICE_VERSION_UPDATE_REQUIRED -> Log.i("P", "googleService update required")
            SERVICE_DISABLED -> Log.i("P", "googleService disabled")
            else -> Log.i("P", "googleService not available")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.i("P", "destroy lifecycle")
        //activity = null
    }
}