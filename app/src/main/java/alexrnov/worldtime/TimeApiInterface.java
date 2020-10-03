package alexrnov.worldtime;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import io.reactivex.Observable;

interface TimeApiInterface {
  //if url is known in advance
  //@GET("America/Whitehorse.json")
  //Call<Time> getTime();

  // when retrofit used, then used Observable
  @GET
  Observable<Time> getTimeWithRx(@Url String url);

  // when retrofit don't used, then used Call
  @GET
  Call<Time> getTimeWithoutRx(@Url String url);
}
