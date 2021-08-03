package infusion.mobile.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class DataViewModel: ViewModel() {

    private val userService = RetrofitBuilder().getUserService()
    var job: Job? = null

    val datas = MutableLiveData<List<Data>>()
    val progress = MutableLiveData<Boolean>()

    fun loadData() {
        getData()
    }

    private fun getData() {
        progress.value = true
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = userService.getUsers()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    datas.value = response.body()?.data
                    progress.value = false
                }
            }
        }
        progress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}