package alexrnov.worldtime.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TimeListViewModel : ViewModel() {

    private val timeList = MutableLiveData<List<String>>()
    fun getTimeList(): LiveData<List<String>> = timeList

    fun loadListFromServer() {
        val list: MutableList<String> = ArrayList()
        list.add("item1"); list.add("item2"); list.add("item3"); list.add("item4"); list.add("item5")
        timeList.value = list
    }
}