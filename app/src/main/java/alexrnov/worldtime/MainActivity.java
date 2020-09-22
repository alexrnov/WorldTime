package alexrnov.worldtime;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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
        for (Movie movie: list) {
          Log.i("P", "url = " + movie.getImageUrl() +
                  ", title = " + movie.getTitle() + ", id = " + movie.getId());
        }
      }

      @Override
      public void onFailure(Call<List<Movie>> call, Throwable t) {
        Log.i("P","Response failure= "+t.toString());
      }
    });



    TimeApiInterface timeApiService = TimeApiClient.getClient().create(TimeApiInterface.class);
    Call<Time> timeCall = timeApiService.getTime();

    timeCall.enqueue(new Callback<Time>() {
      @Override
      public void onResponse(Call<Time> call, Response<Time> response) {
        Log.i("P", "response success = " + response.body());
        Time list = response.body();
        String v = list.getDateTime();
        Log.i("P", "response success = " + v);
      }

      @Override
      public void onFailure(Call<Time> call, Throwable t) {
        Log.i("P","Response failure= "+t.toString());
      }
    });




  }



}