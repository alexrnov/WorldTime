package alexrnov.worldtime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface TimeApiInterfaceWithoutRx {
  // when retrofit don't used, then used Call
  @GET
  Call<Time> getTimeWithoutRx(@Url String url);
}
