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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Привязанная служба, на основе использования расширенного класса Binder */
public class PositionService extends Service {

  private final IBinder binder = new LocalBinder();

  private final Random r = new Random();

  TimeApiInterface timeApiService = TimeApiClient.getClient().create(TimeApiInterface.class);

  String v = "";

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

    /*
    while (true) {
      Log.i("P", "while = ");
      if (false) break;
    }
    */


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

    return v;
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
