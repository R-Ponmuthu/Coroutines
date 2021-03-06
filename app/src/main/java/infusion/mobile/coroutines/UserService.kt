package infusion.mobile.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("users")
    suspend fun getUsers(@Query("delay") delay: Int): Response<Users>
}