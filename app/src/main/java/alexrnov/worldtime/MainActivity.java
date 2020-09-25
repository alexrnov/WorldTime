package alexrnov.worldtime;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import okhttp3.Headers;

import okhttp3.OkHttpClient;
//import okhttp3.Callback;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private final OkHttpClient client = new OkHttpClient();

  private FusedLocationProviderClient fusedLocationClient;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navView, navController);


    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    Call<List<Movie>> call = apiService.getMovies();

    call.enqueue(new Callback<List<Movie>>() {
      @Override
      public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
        Log.i("P", "response success = " + response.body());
        List<Movie> list = response.body();
        for (Movie movie : list) {
          Log.i("P", "url = " + movie.getImageUrl() +
                  ", title = " + movie.getTitle() + ", id = " + movie.getId());
        }
      }

      @Override
      public void onFailure(Call<List<Movie>> call, Throwable t) {
        Log.i("P", "Response failure= " + t.toString());
      }
    });


    TimeApiInterface timeApiService = TimeApiClient.getClient().create(TimeApiInterface.class);
    Call<Time> timeCall = timeApiService.getTime("America/Whitehorse.json");

    timeCall.enqueue(new Callback<Time>() {
      @Override
      public void onResponse(Call<Time> call, Response<Time> response) {
        Time list = response.body();
        String v = list.getDateTime();
        Log.i("P", "response success = " + v);
      }

      @Override
      public void onFailure(Call<Time> call, Throwable t) {
        Log.i("P", "Response failure= " + t.toString());
      }
    });


    timeCall = timeApiService.getTime("Pacific/Majuro.json");

    timeCall.enqueue(new Callback<Time>() {
      @Override
      public void onResponse(Call<Time> call, Response<Time> response) {
        Time list = response.body();
        String v = list.getDateTime();
        Log.i("P", "response success = " + v);
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

              }
            });


    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }

    fusedLocationClient.getLastLocation()
            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                  // Logic to handle location object
                  Log.i("P", "latitude = " + location.getLatitude()
                    + "longitude = " + location.getLongitude());

                }
              }
            });

  }
}