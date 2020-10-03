package alexrnov.worldtime;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class TimeApiClient {

  public static String BASE_URL ="http://worldtimeapi.org/api/timezone/";

  private static Retrofit retrofit;

  public static Retrofit getClientWithRx() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // for rxJava
              .build();
    }
    return retrofit;
  }

  public static Retrofit getClientWithoutRx() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit;
  }

}
