package test.itgungnir.adapter.bean

import test.itgungnir.adapter.ui.delegate.NetworkItemBean

data class ArticleDTO(
    val curPage: Int,
    val datas: List<NetworkItemBean>,
    val over: Boolean,
    val pageCount: Int
)
