package alexrnov.worldtime.ui.sun

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class LocationObserver(
        private val context: Context,
        private val activity: FragmentActivity,
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        Log.i("P", "create lifecycle")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Log.i("P", "start lifecycle")

        locationClient = LocationServices.getFusedLocationProviderClient(activity)
        // required before get location
        if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) { // required for Android M (SDK API 23+)
                ActivityCompat.requestPermissions(
                        activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            }
        } else {
            locationClient?.getLastLocation()?.addOnSuccessListener(activity, locationListener)
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
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.i("P", "destroy lifecycle")
    }
}