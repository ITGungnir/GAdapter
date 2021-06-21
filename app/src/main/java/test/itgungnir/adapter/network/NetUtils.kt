package test.itgungnir.adapter.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetUtils {

    fun <T> withService(serviceClz: Class<T>) = Retrofit.Builder()
        .baseUrl("https://www.wanandroid.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(serviceClz)
}
