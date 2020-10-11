package alexrnov.worldtime

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.tasks.OnSuccessListener

class LocationObserver(
        private val context: Context,
        private val lifecycle: Lifecycle,
        private val callback: OnSuccessListener<Location>) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        Log.i("P", "start lifecycle")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.i("P", "stop lifecycle")
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