package my.itgungnir.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.footer.FooterStatus

/**
 * 创建一个即将绑定到该RecyclerView上的GAdapter适配器
 *
 * 注意调用这个方法后不会立即将生成的GAdapter对象设置给RecyclerView，而是在调用了initialize()方法后才会正式绑定
 */
fun RecyclerView.getGAdapter(): GAdapter = GAdapter(this)

/**
 * 设置RecyclerView上拉加载更多的条件和回调
 * 当调用了这个方法时，则默认RecyclerView具备了上拉加载的能力；如果没有调用这个方法，则默认认为RecyclerView不具备上拉加载的能力
 *
 * @param canLoadMore 一个lambda表达式，用来判断当前情况下是否允许上拉刷新，
 *                    通过这个方法可以控制诸如"SwipeRefreshLayout正在刷新时不能上拉加载"的场景
 * @param onLoadMore 一个lambda表达式，即实际的上拉加载动作的具体执行方式
 */
fun RecyclerView.setOnLoadMoreListener(canLoadMore: () -> Boolean, onLoadMore: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var shouldLoadMore: Boolean = false

        /**
         * RecyclerView在滑动过程中会多次调用此方法
         * 此方法用于判断当前RecyclerView的滚动位置是否支持进行上拉加载
         * 即最后一个item是否完全展示出来了
         */
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (adapter == null || adapter !is GAdapter) {
                return
            }
            val itemCount = adapter?.itemCount ?: 0
            // 适配不同的LayoutManager
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
            // 用户向下滑动时，将此值置为false
            if (dy <= 0) {
                shouldLoadMore = false
            }
        }

        /**
         * RecyclerView在每次滑动状态发生变化时（滚动/静止等）会调用这个方法
         * 通过这个方法可以确定上拉加载的时机，即RecyclerView已经滑倒最底部了，且已经停止了滑动时
         */
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (adapter == null || adapter !is GAdapter) {
                return
            }
            val listAdapter = adapter!! as GAdapter
            // 只有在SCROLL_STATE_IDLE状态下才允许滑动
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // RecyclerView在满足以下条件时才可以进行上拉加载：
                // 1. RecyclerView已经滑动到了最底部；
                // 2. 当前的外界因素满足上拉加载的条件，这一条是通过canLoadMore()方法决定的，是用户自己编写代码决定的；
                // 3. Footer当前的状态是IDLE状态
                if (shouldLoadMore && canLoadMore() && listAdapter.currFooterStatus == FooterStatus.Status.IDLE) {
                    listAdapter.setFooterStatus(FooterStatus.Status.PROGRESSING)
                    listAdapter.refresh(justRefreshFooter = true)
                    // 真正开始上拉加载
                    onLoadMore()
                }
            }
        }
    })
}
