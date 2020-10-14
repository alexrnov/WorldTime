package alexrnov.worldtime.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class TimeListViewModel : ViewModel() {
    private var client: OkHttpClient? = null
    private var callback: Callback? = null


    private val timeList = MutableLiveData<List<String>>()
    fun getTimeList(): LiveData<List<String>> = timeList

    fun loadListFromServer() {
        //val list: MutableList<String> = ArrayList()
        //list.add("item1"); list.add("item2"); list.add("item3"); list.add("item4"); list.add("item5")
        //timeList.value = list



        client = OkHttpClient()
        val request = Request.Builder() //.url("http://worldtimeapi.org/api/timezone.txt")
                .url("http://worldtimeapi.org/api/timezone.json")
                .build()
        callback = getCallback()
        client?.newCall(request)?.enqueue(callback)
    }


    private fun getCallback(): Callback? {
        return object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call,
                                    response: Response) {
                val str = response.body()!!.string()
                val jsonarray: JSONArray
                val list: MutableList<String> = ArrayList()
                try {
                    jsonarray = JSONArray(str)
                    for (i in 0 until jsonarray.length()) {
                        val jsonArray = jsonarray[i]
                        list.add(jsonArray as String)
                        Log.i("P", "time zone = $jsonArray")
                    }


                    //list.add("item1"); list.add("item2"); list.add("item3"); list.add("item4"); list.add("item5")
                    timeList.postValue(list)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("P", "request onFailure")
            }
        }
    }
}