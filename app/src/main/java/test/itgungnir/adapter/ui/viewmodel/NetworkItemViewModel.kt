package test.itgungnir.adapter.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import libs.itgungnir.adapter.RecyclableItem
import test.itgungnir.adapter.network.NetService
import test.itgungnir.adapter.network.NetUtils

class NetworkItemViewModel : ViewModel() {

    private var currentNetworkItemResult: Flow<PagingData<RecyclableItem>>? = null

    fun getNetworkItemList(): Flow<PagingData<RecyclableItem>> {
        val newResult = getArticleListStream().cachedIn(viewModelScope)
        currentNetworkItemResult = newResult
        return newResult
    }

    private fun getArticleListStream(): Flow<PagingData<RecyclableItem>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { NetworkPagingSource() }
    ).flow

    private class NetworkPagingSource : PagingSource<Int, RecyclableItem>() {
        override fun getRefreshKey(state: PagingState<Int, RecyclableItem>): Int? =
            state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecyclableItem> {
            val pageNo = params.key ?: 0
            return try {
                val response = NetUtils.withService(NetService::class.java).getArticleList(pageNo)
                val prevKey = when (pageNo == 0) {
                    true -> null
                    else -> 0
                }
                val nextKey = when (response.data.over) {
                    true -> null
                    else -> pageNo + 1
                }
                LoadResult.Page(data = response.data.datas, prevKey = prevKey, nextKey = nextKey)
            } catch (exception: Exception) {
                return LoadResult.Error(exception)
            }
        }
    }
}
