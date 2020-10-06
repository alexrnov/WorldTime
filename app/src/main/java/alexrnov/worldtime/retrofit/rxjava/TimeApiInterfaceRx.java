package alexrnov.worldtime.retrofit.rxjava;

import alexrnov.worldtime.retrofit.Time;
import retrofit2.http.GET;
import retrofit2.http.Url;
import io.reactivex.Observable;

public interface TimeApiInterfaceRx {
  //if url is known in advance
  //@GET("America/Whitehorse.json")
  //Call<Time> getTime();

  // when retrofit used, then used Observable
  @GET
  Observable<Time> getTime(@Url String url);
}
