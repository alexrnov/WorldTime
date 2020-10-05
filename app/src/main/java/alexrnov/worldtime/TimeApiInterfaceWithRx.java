package alexrnov.worldtime;

import retrofit2.http.GET;
import retrofit2.http.Url;
import io.reactivex.Observable;

interface TimeApiInterfaceWithRx {
  //if url is known in advance
  //@GET("America/Whitehorse.json")
  //Call<Time> getTime();

  // when retrofit used, then used Observable
  @GET
  Observable<Time> getTimeWithRx(@Url String url);
}
