package com.example.pov_clone_project.Database

//setup of retrofit
import com.example.pov_clone_project.screens.Medicine
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.GET

object RetrofitClient {
    private const val BASE_URL = "https://beta.myupchar.com/api/" // Your API base URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}



//API Interface
interface MedicineApi {
    @GET("medicines")
    fun getMedicines(): Call<List<Medicine>>
}
