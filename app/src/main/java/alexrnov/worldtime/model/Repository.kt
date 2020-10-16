package alexrnov.worldtime.model

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository modules handle data operations. They provide a clean API so that the rest of
 * the app can retrieve this data easily. They know where to get the data from and what
 * API calls to make when data is updated. You can consider repositories to be mediators
 * between different data sources, such as persistent models, web services, and caches.
 */
// @Inject lets Dagger know how to create instances of this object
// Scope this class to a component using @Singleton scope (i.e. ApplicationGraph)
@Singleton
class Repository @Inject constructor(val context: Context) {

    fun getRepositories(completion: (List<String>?, Error?) -> Unit) {

        val client = OkHttpClient()

        val request = Request.Builder() //.url("http://worldtimeapi.org/api/timezone.txt")
                .url("http://worldtimeapi.org/api/timezone.json")
                .build()

        client.newCall(request).enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call,
                                    response: Response) {
                val s = ArrayList<String>()
                val str = response.body()!!.string()
                val jsonarray: JSONArray
                try {
                    jsonarray = JSONArray(str)
                    for (i in 0 until jsonarray.length()) {
                        val jsonArray = jsonarray[i]
                        s.add(jsonArray as String)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                completion(s, null)
            }

            override fun onFailure(call: Call, e: IOException) {
                completion(null, Error(e.message))
            }
        })
    }
}