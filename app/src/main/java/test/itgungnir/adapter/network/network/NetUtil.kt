package test.itgungnir.adapter.network.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class NetUtil {

    companion object {

        fun <T> withService(serviceClz: Class<T>) = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(serviceClz)
    }
}