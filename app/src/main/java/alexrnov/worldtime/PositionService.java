package alexrnov.worldtime;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Привязанная служба, на основе использования расширенного класса Binder */
public class PositionService extends Service {

  private final IBinder binder = new LocalBinder();

  private final Random r = new Random();
  private String sAccept = "";
  TimeApiInterface timeApiService = TimeApiClient.getClient().create(TimeApiInterface.class);

  String v = "";

  private CompositeDisposable compositeDisposable = new CompositeDisposable();


  /*
   * Класс используется для связи с клиентом. Так как служба всегда
   * запускается в том же самом процессе, что и клиент, нам не нужно
   * иметь дело с межпроцессорным взаимодействием.
   */
  public class LocalBinder extends Binder {

    LocalBinder() {

    }

    PositionService getService() {
      /*
       * Возвращает экземпляр данной службы клиенту, который может
       * вызвать его public-методы
       */
      return PositionService.this;
    }
  }

  //метод, который может вызвать клиент
  public String getRandomNumber(String path) {

    compositeDisposable.add(timeApiService.getTime(path)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(postMessage -> {
              sAccept = postMessage.getDateTime();
              Log.i("P", "sAccept rx service = " + sAccept);
            }));

    //Log.i("P", "sAccept rx service2 = " + sAccept);
    /*
    while (true) {
      Log.i("P", "while = ");
      if (false) break;
    }
    */

    /*
    Call<Time> timeCall = timeApiService.getTime(path);
    timeCall.enqueue(new Callback<Time>() {
      @Override
      public void onResponse(Call<Time> call, Response<Time> response) {
        Time list = response.body();
        v = list.getDateTime();
        Log.i("P", "service response success = " + v);
      }

      @Override
      public void onFailure(Call<Time> call, Throwable t) {
        Log.i("P", "Response failure= " + t.toString());
      }
    });
    */
    return v;
  }

  public Observable<Time> obs() {
    Observable<Time> observable = timeApiService.getTime("America/Whitehorse.json");
    return observable;
    /*
    return observable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(time -> {

            });
     */


  }

  // invoke when services are created by bindService()
  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  /*
  // invoke when services are created by startService()
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    // If we get killed, after returning from here, restart
    return START_STICKY;
  }
  */
}
