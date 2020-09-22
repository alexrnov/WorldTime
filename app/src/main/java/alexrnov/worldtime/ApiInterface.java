package alexrnov.worldtime;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiInterface {
  @GET("volley_array.json")
  Call<List<Movie>> getMovies(); // T return value is always a parameterized Call<T>
}
