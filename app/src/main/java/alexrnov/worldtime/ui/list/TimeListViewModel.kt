package alexrnov.worldtime.ui.list

import alexrnov.worldtime.model.Repository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeListViewModel : ViewModel() {

    private val repository = Repository()
    private var time = MutableLiveData<List<String>>()

    fun getTimeList(): LiveData<List<String>> = time

    fun loadListFromServer() {
        time.postValue(repository.timeListFromServer)
        val list = time.value
        Log.i("P", "loadListFromServer = " + list?.size)
    }
}