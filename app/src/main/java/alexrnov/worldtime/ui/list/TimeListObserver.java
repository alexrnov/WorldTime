package alexrnov.worldtime.ui.list;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

class TimeListObserver implements LifecycleObserver {

    private TimeListViewModel timeListViewModel;

    public TimeListObserver(TimeListViewModel timeListViewModel) {
        this.timeListViewModel = timeListViewModel;
    }

    private OkHttpClient client;
    private okhttp3.Callback callback;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        timeListViewModel.loadListFromServer();
        /*
        client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("http://worldtimeapi.org/api/timezone.txt")
                .url("http://worldtimeapi.org/api/timezone.json")
                .build();
        callback = getCallback();
        client.newCall(request).enqueue(callback);

         */
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        //client = null;
        //callback = null;
    }

    private okhttp3.Callback getCallback() {
        return new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call,
                                   @NotNull okhttp3.Response response) throws IOException {
                String str = response.body().string();

                JSONArray jsonarray;
                try {
                    jsonarray = new JSONArray(str);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Object jsonArray = jsonarray.get(i);
                        Log.i("P", "time zone = " + jsonArray.toString());
                    }
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