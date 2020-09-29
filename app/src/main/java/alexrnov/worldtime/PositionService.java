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

/** Привязанная служба, на основе использования расширенного класса Binder */
public class PositionService extends Service {

  private final IBinder binder = new LocalBinder();

  private final Random r = new Random();

  private Activity activity;

  private FusedLocationProviderClient fusedLocationClient;
  /*
   * Класс используется для связи с клиентом. Так как служба всегда
   * запускается в том же самом процессе, что и клиент, нам не нужно
   * иметь дело с межпроцессорным взаимодействием.
   */
  public class LocalBinder extends Binder {


    PositionService getService() {
      /*
       * Возвращает экземпляр данной службы клиенту, который может
       * вызвать его public-методы
       */
      return PositionService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  //метод, который может вызвать клиент
  public Integer getRandomNumber() {



    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      //Toast.makeText(YourService.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();

      /*
      ActivityCompat.requestPermissions(
              this,
              new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
              1);
      */

    }

    fusedLocationClient.getLastLocation()
            .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(Location location) {
                Log.i("P", "service location onSuccess");
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                  // Logic to handle location object
                  Log.i("P", "latitude = " + location.getLatitude()
                          + "longitude = " + location.getLongitude());

                }
              }
            });




    return r.nextInt();
  }



  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    // If we get killed, after returning from here, restart
    return START_STICKY;
  }






}
