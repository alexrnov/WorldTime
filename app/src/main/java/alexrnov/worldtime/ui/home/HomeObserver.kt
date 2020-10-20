package alexrnov.worldtime.ui.home

import alexrnov.worldtime.TimeService
import alexrnov.worldtime.TimeService.LocalBinder
import alexrnov.worldtime.retrofit.Time
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeObserver(
        var fragment: Fragment?,
        val homeViewModel: HomeViewModel) : LifecycleObserver {

    var bound = false
    private var timeService: TimeService? = null
    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        val intent = Intent(fragment?.activity, TimeService::class.java)
        //BIND_AUTO_CREATE - параметр привязки: создать службу если она еще не выполняется
        //BIND_AUTO_CREATE - параметр привязки: создать службу если она еще не выполняется
        fragment?.activity?.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        if (bound) { // открепиться от сервиса
            fragment?.activity?.unbindService(mConnection)
            bound = false
        }
        fragment = null
    }


    //определяет обратный вызов для связанной службы, передаваемый в bindService()
    private val mConnection: ServiceConnection = object : ServiceConnection {
        /*
     * Система вызывает этот метод, чтобы выдать объект IBinder, возвращенный
     * методом onBind() службы.
     */
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.i("P", "onServiceConnected init")
            //мы получаем связь с TimeService, преобразуем интерфейс IBinder в LocalBinder
            //и получаем экземпляр TimeService
            val binder = service as LocalBinder
            timeService = binder.service
            bound = true
            //вызывается public-метод связанной службы. Однако если c этим вызовом
            //было что-то, что могло привести к зависанию(длительной работы метода),
            //тогда этот запрос должен происходить в отдельном потоке, чтобы избежать
            //снижения производительности активити-класса
            val subscribe: Disposable? = timeService?.getTimeObservable("America/Whitehorse.json")
                    ?.subscribeOn(Schedulers.newThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({ time: Time -> Log.i("P", "service time = " + time.dateTime) },
                            { error: Throwable? -> Log.i("P", "service error" + error.toString()) })

            subscribe?.let { compositeDisposable.add(it) }
        }

        /*
     * Система Android вызывает этот метод в случае непредвиденной потери
     * подключения к службе, например при сбое в работе службы или в случае
     * ее завершения. Этот метод не вызывается, когда клиент отменяет привязку
     */
        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }
}