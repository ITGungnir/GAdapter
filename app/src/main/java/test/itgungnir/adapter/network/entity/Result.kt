package test.itgungnir.adapter.network.entity

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-06
 */
data class Result<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)