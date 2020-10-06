package alexrnov.worldtime.retrofit.standard;

import alexrnov.worldtime.retrofit.Time;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface TimeApiInterface {
  // when retrofit don't used, then used Call
  @GET
  Call<Time> getTime(@Url String url);
}
