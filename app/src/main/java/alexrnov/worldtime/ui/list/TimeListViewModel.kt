package alexrnov.worldtime.ui.list

import alexrnov.worldtime.model.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TimeListViewModel @Inject constructor(
        private val repository: Repository
): ViewModel() {

    //private val repository = Repository()
    private var time = MutableLiveData<Pair<List<String>?, Error?>>()

    fun getTimeList(): LiveData<Pair<List<String>?, Error?>> = time

    fun loadListFromServer() {
        viewModelScope.launch {
            loadList()
        }
    }

    // Dispatheds.Default used common phone threads pool
    private suspend fun loadList() = withContext(Dispatchers.Default) {
        repository.getRepositories { repos, error ->
            time.postValue(Pair(repos, error))
        }
    }
}