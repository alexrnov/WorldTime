package alexrnov.worldtime.ui.list

import alexrnov.worldtime.model.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeListViewModel : ViewModel() {

    private val repository = Repository()
    private var time = MutableLiveData<Pair<List<String>?, Error?>>()

    fun getTimeList(): LiveData<Pair<List<String>?, Error?>> = time

    fun loadListFromServer() {
        repository.getRepositories { repos, error ->
            time.postValue(Pair(repos, error))
        }
    }
}