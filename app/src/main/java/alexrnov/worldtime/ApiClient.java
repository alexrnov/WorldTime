package alexrnov.worldtime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ApiClient {
  public static String BASE_URL ="http://velmm.com/apis/";
  private static Retrofit retrofit;
  public static Retrofit getClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit;
  }
}
