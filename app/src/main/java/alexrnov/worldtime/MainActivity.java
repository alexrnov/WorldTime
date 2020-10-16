package alexrnov.worldtime;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import alexrnov.worldtime.model.ActivityComponent;
import alexrnov.worldtime.retrofit.Time;
import alexrnov.worldtime.retrofit.standard.TimeApiClient;
import alexrnov.worldtime.retrofit.rxjava.TimeApiClientRx;
import alexrnov.worldtime.retrofit.standard.TimeApiInterface;
import alexrnov.worldtime.retrofit.rxjava.TimeApiInterfaceRx;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  //private TimeService timeService;

  //boolean mBound = false;

  private String sAccept;


  // Reference to the Login graph. Notice that the variable loginComponent is not
  // annotated with @Inject because you're not expecting that variable to be provided by Dagger.
  // LoginComponent is created in the activity's onCreate() method, and it'll get implicitly destroyed when the activity gets destroyed.
  public ActivityComponent activityComponent;

  // When using activities, inject Dagger in the activity's onCreate() method
  // before calling super.onCreate() to avoid issues with fragment restoration.
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    // creation of the login graph using the application graph
    activityComponent = ((Initialization) getApplicationContext()).applicationComponent.activityComponent().create();
    // make Dagger instantiate @Inject fields in MainActivity
    activityComponent.inject(this);


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);




    Log.i("P", "onCreate() method");
    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_sun, R.id.navigation_list)
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
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.i("P", "oneResume() method");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i("P", "onStart() method");

    //Intent intent = new Intent(this, TimeService.class);
    //BIND_AUTO_CREATE - параметр привязки: создать службу если она еще не выполняется
    //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

  }

  // Long-term operations (making network calls or performing database transactions)
  // must be performed in onStop()
  @Override
  protected void onStop() {
    super.onStop();
    //открепиться от сервиса
    //if (mBound) {
      //unbindService(mConnection);
      //mBound = false;
    //}
    Log.i("P", "onStop() method");
  }

  // called when the permission request window is displayed
  @Override
  protected void onPause() {
    super.onPause();
    Log.i("P", "onPause() method");
  }

  // here need release all resources that have not yet been released by earlier callbacks such as onStop().
  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.i("P", "onDestroy() method");
  }

  // call after onStop() if user return to app
  @Override
  protected void onRestart() {
    super.onRestart();
    Log.i("P", "onRestart() method");
  }

  // request permission (required for invoke onRequestPermissionsResult in fragment)
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NotNull String[] permissions, @NotNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}