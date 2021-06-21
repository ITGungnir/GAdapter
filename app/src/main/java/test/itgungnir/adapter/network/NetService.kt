package test.itgungnir.adapter.network

import retrofit2.http.GET
import retrofit2.http.Path
import test.itgungnir.adapter.bean.ArticleDTO
import test.itgungnir.adapter.bean.NetResult

interface NetService {

    @GET("/article/list/{pageNo}/json")
    suspend fun getArticleList(@Path("pageNo") pageNo: Int): NetResult<ArticleDTO>
}
