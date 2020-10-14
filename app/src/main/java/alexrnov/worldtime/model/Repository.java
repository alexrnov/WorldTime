package alexrnov.worldtime.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Repository modules handle data operations. They provide a clean API so that the rest of
 * the app can retrieve this data easily. They know where to get the data from and what
 * API calls to make when data is updated. You can consider repositories to be mediators
 * between different data sources, such as persistent models, web services, and caches.
 */
public class Repository {

    private OkHttpClient client;
    private okhttp3.Callback callback;
    List<String> list = new ArrayList<>();
    private MutableLiveData<List<String>> timeList = new MutableLiveData<>();

    public List<String> getTimeListFromServer() {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("http://worldtimeapi.org/api/timezone.txt")
                .url("http://worldtimeapi.org/api/timezone.json")
                .build();
        callback = getCallback();
        client.newCall(request).enqueue(callback);
        //List<String> list = new ArrayList<>();
        //list.add("1"); list.add("2"); list.add("3"); list.add("4"); list.add("5");
        //timeList.setValue(list);
        return list;
    }

    private okhttp3.Callback getCallback() {
        return new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call,
                                   @NotNull okhttp3.Response response) throws IOException {
                String str = response.body().string();

                JSONArray jsonarray;
                //List<String> list = new ArrayList<String>();
                try {
                    jsonarray = new JSONArray(str);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Object jsonArray = jsonarray.get(i);
                        Log.i("P", "time zone = " + jsonArray.toString());
                        list.add(jsonArray.toString());
                    }
                    //timeList.postValue(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                Log.i("P", "request onFailure");
            }
        };
    }
}
