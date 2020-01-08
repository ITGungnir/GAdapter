package my.itgungnir.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.footer.FooterDelegate
import my.itgungnir.adapter.footer.FooterStatus
import my.itgungnir.adapter.footer.FooterVO

val PL_FOOTER_STATUS = "PL_FOOTER_STATUS"

fun RecyclerView.getGAdapter(): GAdapter = GAdapter(this)

fun RecyclerView.setOnLoadMoreListener(canLoadMore: () -> Boolean, onLoadMore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var shouldLoadMore: Boolean = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (adapter == null || adapter !is GAdapter) {
                return
            }
            val itemCount = adapter?.itemCount ?: 0
            when (val manager = recyclerView.layoutManager) {
                is LinearLayoutManager ->
                    shouldLoadMore = manager.findLastCompletelyVisibleItemPosition() == itemCount - 1
                is GridLayoutManager ->
                    shouldLoadMore = manager.findLastCompletelyVisibleItemPosition() == itemCount - 1
                is StaggeredGridLayoutManager -> {
                    var indexes = IntArray(manager.spanCount)
                    indexes = manager.findLastCompletelyVisibleItemPositions(indexes)
                    shouldLoadMore = indexes.contains(itemCount - 1)
                }
            }
            if (dy <= 0) {
                shouldLoadMore = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (adapter == null || adapter !is GAdapter) {
                return
            }
            val listAdapter = adapter!! as GAdapter
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (shouldLoadMore && canLoadMore() && listAdapter.currFooterStatus == FooterStatus.Status.IDLE) {
                    listAdapter.setFooterStatus(FooterStatus.Status.PROGRESSING)
                    listAdapter.refresh(justRefreshFooter = true)
                    onLoadMore()
                }
            }
        }
    })
}

fun GAdapter.addFooterDelegate(delegate: FooterDelegate) = addDelegate({ it is FooterVO }, delegate)

fun GAdapter.refreshWithFooter(list: MutableList<ListItem>, hasMore: Boolean) {
    if (hasMore) {
        setFooterStatus(FooterStatus.Status.IDLE)
    } else {
        setFooterStatus(FooterStatus.Status.NO_MORE)
    }
    refresh(dataList = list, justRefreshFooter = false)
}

fun GAdapter.loadMoreError() {
    setFooterStatus(FooterStatus.Status.FAILED)
    refresh(justRefreshFooter = true)
}

fun GAdapter.loadMoreRetry(retry: () -> Unit) {
    setFooterStatus(FooterStatus.Status.PROGRESSING)
    refresh(justRefreshFooter = true)
    retry.invoke()
}
