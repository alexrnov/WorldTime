package alexrnov.worldtime;

import retrofit2.Call;
import retrofit2.http.GET;

interface TimeApiInterface {
  @GET("America/Whitehorse.json")
  Call<Time> getTime();
}
