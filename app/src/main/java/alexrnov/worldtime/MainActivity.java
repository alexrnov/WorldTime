package alexrnov.worldtime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import alexrnov.worldtime.retrofit.Time;
import alexrnov.worldtime.retrofit.standard.TimeApiClient;
import alexrnov.worldtime.retrofit.rxjava.TimeApiClientRx;
import alexrnov.worldtime.retrofit.standard.TimeApiInterface;
import alexrnov.worldtime.retrofit.rxjava.TimeApiInterfaceRx;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
//import okhttp3.Callback;


import alexrnov.worldtime.TimeService.LocalBinder;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  private final OkHttpClient client = new OkHttpClient();
  private FusedLocationProviderClient fusedLocationClient;

  private TimeService timeService;

  boolean mBound = false;

  private String sAccept;

  //определяет обратный вызов для связанной службы, передаваемый в bindService()
  private ServiceConnection mConnection = new ServiceConnection() {

    /*
     * Система вызывает этот метод, чтобы выдать объект IBinder, возвращенный
     * методом onBind() службы.
     */
    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      Log.i("P", "onServiceConnected init");
      //мы получаем связь с Service3, преобразуем интерфейс IBinder в LocalBinder
      //и получаем экземпляр Service3
      LocalBinder binder = (LocalBinder) service;
      timeService = binder.getService();
      mBound = true;
      //вызывается public-метод связанной службы. Однако если c этим вызовом
      //было что-то, что могло привести к зависанию(длительной работы метода),
      //тогда этот запрос должен происходить в отдельном потоке, чтобы избежать
      //снижения производительности активити-класса


      Disposable subscribe = timeService.getTimeObservable("America/Whitehorse.json")
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(time -> {
                Log.i("P", "obs = " + time.getDateTime());
              }, error -> {
                Log.i("P", "obs error");
              });

      compositeDisposable.add(subscribe);
    }

    /*
     * Система Android вызывает этот метод в случае непредвиденной потери
     * подключения к службе, например при сбое в работе службы или в случае
     * ее завершения. Этот метод не вызывается, когда клиент отменяет привязку
     */
    @Override
    public void onServiceDisconnected(ComponentName arg0) {
      mBound = false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.i("P", "onCreate() method");
    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navView, navController);


    TimeApiInterfaceRx timeApiService = TimeApiClientRx.getClient().create(TimeApiInterfaceRx.class);
    Disposable subscribe = timeApiService.getTime("America/Whitehorse.json")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(postMessage -> {
              sAccept = postMessage.getDateTime();
              Log.i("P", "sAccept rx= " + sAccept);
            }, error -> {
              Log.i("P", "error rx = " + error.getMessage());
            });

    compositeDisposable.add(subscribe);


    TimeApiInterface timeApiService2 = TimeApiClient.getClient().create(TimeApiInterface.class);
    Call<Time> timeCall = timeApiService2.getTime("Pacific/Majuro.json");
    timeCall.enqueue(new Callback<Time>() {
      @Override
      public void onResponse(Call<Time> call, Response<Time> response) {
        Time list = response.body();
        String v = list.getDateTime();
        Log.i("P", "timeCall onResponse = " + v);
      }

      @Override
      public void onFailure(Call<Time> call, Throwable t) {
        Log.i("P", "Response failure= " + t.toString());
      }
    });



    Request request = new Request.Builder()
            //.url("http://worldtimeapi.org/api/timezone.txt")
            .url("http://worldtimeapi.org/api/timezone.json")
            .build();

    client.newCall(request).enqueue(
            new okhttp3.Callback() {
              @Override
              public void onResponse(@NotNull okhttp3.Call call,
                                     @NotNull okhttp3.Response response) throws IOException {
                String str = response.body().string();

                JSONArray jsonarray;
                try {
                  jsonarray = new JSONArray(str);
                  for (int i = 0; i < jsonarray.length(); i++) {
                    Object jsonArray = jsonarray.get(i);
                    Log.i("P", "time zone = " + jsonArray.toString());
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.i("P", "request onFailure");
              }
            });






    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      //Toast.makeText(YourService.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();


      ActivityCompat.requestPermissions(
              this,
              new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
              1);

    }

    fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(Location location) {
                Log.i("P", "location onSuccess");
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                  // Logic to handle location object
                  Log.i("P", "latitude = " + location.getLatitude()
                    + "longitude = " + location.getLongitude());

                }
              }
            });
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i("P", "onStart() method");

    Intent intent = new Intent(this, TimeService.class);
    //BIND_AUTO_CREATE - параметр привязки: создать службу если она еще не выполняется
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    //открепиться от сервиса
    if (mBound) {
      unbindService(mConnection);
      mBound = false;
    }
  }
}