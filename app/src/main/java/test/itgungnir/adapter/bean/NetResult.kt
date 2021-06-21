package test.itgungnir.adapter.bean

data class NetResult<T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)
