package alexrnov.worldtime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface TimeApiInterface {
  //@GET("America/Whitehorse.json")
  //Call<Time> getTime();

  @GET
  Call<Time> getTime(@Url String url);
}
