package test.itgungnir.adapter.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import libs.itgungnir.adapter.RecyclableItem
import test.itgungnir.adapter.network.NetService
import test.itgungnir.adapter.network.NetUtils
import test.itgungnir.adapter.ui.delegate.NetworkItemBean

class NetworkItemViewModel : ViewModel() {

    private var currPageNo: Int = 0

    val refreshingState = MutableLiveData<Boolean>()
    val firstPageDataState = MutableLiveData<Pair<Boolean, List<RecyclableItem>>?>()
    val otherPageDataState = MutableLiveData<Pair<Boolean, List<RecyclableItem>>?>()
    val refreshErrorState = MutableLiveData<Exception?>()
    val loadMoreErrorState = MutableLiveData<Exception?>()

    fun refreshDataList() = viewModelScope.launch {
        refreshingState.value = true
        currPageNo = 0
        try {
            val response = NetUtils.withService(NetService::class.java).getArticleList(currPageNo).data
            firstPageDataState.value = !response.over to response.datas.map { NetworkItemBean(it.id, it.title) }
        } catch (e: Exception) {
            refreshErrorState.value = e
        } finally {
            refreshingState.value = false
        }
    }

    fun loadMoreDataList() = viewModelScope.launch {
        currPageNo++
        try {
            val response = NetUtils.withService(NetService::class.java).getArticleList(currPageNo).data
            otherPageDataState.value = !response.over to response.datas.map { NetworkItemBean(it.id, it.title) }
        } catch (e: Exception) {
            loadMoreErrorState.value = e
        }
    }
}
