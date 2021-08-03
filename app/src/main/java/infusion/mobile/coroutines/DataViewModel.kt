package infusion.mobile.coroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class DataViewModel : ViewModel() {

    private val userService = RetrofitBuilder().getUserService()
    var job: Job? = null
    private var TAG = "DataViewModel"

    val datas = MutableLiveData<List<Data>>()
    val progress = MutableLiveData<Boolean>()

    fun getData() {

        progress.value = true

//        job = GlobalScope.launch(Dispatchers.Main) {
//            progress.value = true
//            try {
//                val response = userService.getUsers(3)
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        datas.value = response.body()?.data
//                        progress.value = false
//                    }
//                }
//            } catch (ex: Exception) {
//                Log.d(TAG, ex.toString())
//            } finally {
//                progress.value = false
//            }
//        }

        job = CoroutineScope(Dispatchers.IO).launch {

            progress.postValue(true)

            val response = userService.getUsers(3)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    datas.postValue(response.body()?.data)
                    progress.postValue(false)
                }
            }
        }
        progress.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}