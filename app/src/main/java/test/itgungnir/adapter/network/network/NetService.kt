package test.itgungnir.adapter.network.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import test.itgungnir.adapter.network.entity.ArticleDTO
import test.itgungnir.adapter.network.entity.Result

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface NetService {

    @GET("article/list/{pageNo}/json")
    fun getArticleList(@Path("pageNo") pageNo: Int): Single<Result<ArticleDTO>>
}