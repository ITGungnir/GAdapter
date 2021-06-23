package libs.itgungnir.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.*
import libs.itgungnir.adapter.footer.FooterDelegate
import libs.itgungnir.adapter.footer.FooterStatus
import libs.itgungnir.adapter.footer.FooterVO
import java.lang.ref.WeakReference

/**
 * GAdapter，一种简单易用的RecyclerView.Adapter的封装
 *
 * GAdapter将viewType抽离成Delegate，Delegate与RecyclableItem一一对应，
 * 这样就实现了item的UI渲染和viewType的类型判断逻辑的解耦，便于扩展
 *
 * Created by ITGungnir on 2019-10-05
 */
class GAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var supportLoadMore: Boolean = false
    private var currFooterStatus: FooterStatus.Status = FooterStatus.Status.IDLE
    private var mCanLoadMoreCallback: (() -> Boolean)? = null
    private var mLoadMoreCallback: (() -> Unit)? = null

    private var recyclerViewRef: WeakReference<RecyclerView>? = null

    /**
     * 当前列表中已有的所有RecyclableItem的列表（注意这个List中不包括FooterVO）
     */
    var currItems: MutableList<RecyclableItem> = mutableListOf()

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<RecyclableItem>() {
        override fun areItemsTheSame(oldItem: RecyclableItem, newItem: RecyclableItem): Boolean =
            oldItem.javaClass.name == newItem.javaClass.name && newItem.isItemSameTo(oldItem)

        override fun areContentsTheSame(oldItem: RecyclableItem, newItem: RecyclableItem): Boolean =
            newItem.isContentSameTo(oldItem)

        override fun getChangePayload(oldItem: RecyclableItem, newItem: RecyclableItem): Any? =
            newItem.getChangePayload(oldItem) ?: super.getChangePayload(oldItem, newItem)
    })

    private val items: MutableList<RecyclableItem>
        get() = differ.currentList

    /**
     * 存储适配器中所有RecyclableItem和Delegate的一一对应关系
     */
    private val bindMaps: MutableList<BindMap> = mutableListOf()

    private val loadMoreListener by lazy {
        object : RecyclerView.OnScrollListener() {
            private var shouldLoadMore: Boolean = false

            /**
             * RecyclerView在滑动过程中会多次调用此方法
             * 此方法用于判断当前RecyclerView的滚动位置是否支持进行上拉加载
             * 即最后一个item是否完全展示出来了
             */
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mCanLoadMoreCallback?.invoke() == true) {
                    recyclerView.adapter?.takeIf { it is GAdapter }?.let {
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
                }
            }

            /**
             * RecyclerView在每次滑动状态发生变化时（滚动/静止等）会调用这个方法
             * 通过这个方法可以确定上拉加载的时机，即RecyclerView已经滑倒最底部了，且已经停止了滑动时
             */
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mCanLoadMoreCallback?.invoke() == true) {
                    recyclerView.adapter?.takeIf { it is GAdapter }?.let {
                        val listAdapter = it as? GAdapter ?: return
                        // 只有在SCROLL_STATE_IDLE状态下才允许滑动
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            // RecyclerView在满足以下条件时才可以进行上拉加载：
                            // 1. RecyclerView已经滑动到了最底部；
                            // 2. 当前的外界因素满足上拉加载的条件，这一条是通过canLoadMore()方法决定的，是用户自己编写代码决定的；
                            // 3. Footer当前的状态是IDLE状态
                            if (shouldLoadMore && listAdapter.currFooterStatus == FooterStatus.Status.IDLE) {
                                listAdapter.updateFooter(FooterStatus.Status.PROGRESSING)
                                mLoadMoreCallback?.invoke()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        if (supportLoadMore) {
            recyclerView.addOnScrollListener(loadMoreListener)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef = null
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        bindMaps.first { it.isItemForType(items[position]) }.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val delegate = bindMaps.first { it.type == viewType }.delegate
        if (delegate.adapterRef == null) {
            delegate.adapterRef = WeakReference(this)
        }
        if (delegate.recyclerViewRef == null) {
            delegate.recyclerViewRef = recyclerViewRef
        }
        return delegate.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindMaps.first { it.type == holder.itemViewType }.delegate
            .onBindViewHolder(holder, items[position], mutableListOf())
    }

    /**
     * 三个参数的onBindViewHolder()方法中可以实现item的局部刷新，避免"白光一闪"的问题
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindMaps.first { it.type == holder.itemViewType }.delegate
                .onBindViewHolder(holder, items[position], payloads)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val index = holder.bindingAdapterPosition
        if (index < 0 || index >= items.size) {
            return
        }
        holder.onResumeCallback.invoke(holder, items[index])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val index = holder.bindingAdapterPosition
        if (index < 0 || index >= items.size) {
            return
        }
        holder.onPauseCallback.invoke(holder, items[index])
    }

    fun addDelegate(isViewForType: (RecyclableItem?) -> Boolean, delegate: Delegate): GAdapter = apply {
        bindMaps.add(BindMap(bindMaps.size, isViewForType, delegate))
    }

    fun setFooterDelegate(canLoadMoreCallback: () -> Boolean, delegate: FooterDelegate) = apply {
        supportLoadMore = true
        mCanLoadMoreCallback = canLoadMoreCallback
        addDelegate({ it is FooterVO }, delegate)
    }

    fun onLoadMore(loadMoreCallback: () -> Unit) = apply {
        mLoadMoreCallback = loadMoreCallback
    }

    fun isLoadingMore() = currFooterStatus == FooterStatus.Status.PROGRESSING

    fun refresh(dataList: List<RecyclableItem>, commitCallback: () -> Unit = {}) {
        val newList = mutableListOf<RecyclableItem>()
        dataList.forEach { newList.add(it) }
        currItems.clear()
        currItems.addAll(newList)
        if (supportLoadMore) {
            newList.add(FooterVO(currFooterStatus))
        }
        differ.submitList(newList, commitCallback)
    }

    fun refresh(dataList: List<RecyclableItem>, hasMore: Boolean, commitCallback: () -> Unit = {}) {
        val newList = mutableListOf<RecyclableItem>()
        dataList.forEach { newList.add(it) }
        currItems.clear()
        currItems.addAll(newList)
        if (supportLoadMore) {
            currFooterStatus = when (hasMore) {
                true -> FooterStatus.Status.IDLE
                else -> FooterStatus.Status.NO_MORE
            }
            newList.add(FooterVO(currFooterStatus))
        }
        differ.submitList(newList, commitCallback)
    }

    fun applyAll(applyLambda: (RecyclableItem) -> RecyclableItem, commitCallback: () -> Unit = {}) {
        val newList = currItems.map { applyLambda.invoke(it) }
        refresh(newList.toMutableList(), commitCallback)
    }

    fun insert(index: Int = items.size, data: List<RecyclableItem>, commitCallback: () -> Unit = {}) {
        if (index < 0 || index > currItems.size) {
            return
        }
        currItems.addAll(index, data)
        refresh(currItems, commitCallback)
    }

    fun append(data: List<RecyclableItem>, hasMore: Boolean, commitCallback: () -> Unit = {}) {
        currItems.addAll(data)
        if (supportLoadMore) {
            currFooterStatus = when (hasMore) {
                true -> FooterStatus.Status.IDLE
                else -> FooterStatus.Status.NO_MORE
            }
        }
        refresh(currItems, commitCallback)
    }

    fun update(index: Int, updateLambda: (RecyclableItem) -> RecyclableItem, commitCallback: () -> Unit = {}) {
        if (index < 0 || index >= currItems.size) {
            return
        }
        currItems[index] = updateLambda.invoke(currItems[index])
        refresh(currItems, commitCallback)
    }

    fun updateFooter(footerStatus: FooterStatus.Status, commitCallback: () -> Unit = {}) {
        currFooterStatus = footerStatus
        refresh(currItems, commitCallback)
    }

    fun remove(index: Int, commitCallback: () -> Unit = {}) {
        if (index < 0 || index >= currItems.size) {
            return
        }
        currItems.removeAt(index)
        refresh(currItems, commitCallback)
    }

    fun removeAll(data: List<RecyclableItem>, commitCallback: () -> Unit = {}) {
        currItems.removeAll(data)
        refresh(currItems, commitCallback)
    }
}
