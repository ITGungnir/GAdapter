package my.itgungnir.adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import my.itgungnir.adapter.footer.FooterDelegate
import my.itgungnir.adapter.footer.FooterStatus
import my.itgungnir.adapter.footer.FooterVO

/**
 * GAdapter，一种简单易用的RecyclerView.Adapter的封装
 *
 * GAdapter将viewType抽离成Delegate，Delegate与ListItem一一对应，
 * 这样就实现了item的UI渲染和viewType的类型判断逻辑的解耦，便于扩展
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class GAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<VH>() {

    /**
     * 当前列表中Footer的状态
     */
    var currFooterStatus: FooterStatus.Status = FooterStatus.Status.IDLE

    /**
     * 当前列表中已有的所有ListItem的列表
     * 注意这个List中不包括FooterVO
     */
    private var currDataList: MutableList<out ListItem> = mutableListOf()

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<ListItem>() {
        /**
         * 这个方法中必须先对className进行判断，否则可能报错：ClassCastException
         */
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            oldItem.javaClass.name == newItem.javaClass.name && newItem.isItemSameTo(oldItem)

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            newItem.isContentSameTo(oldItem)

        override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? =
            newItem.getChangePayload(oldItem) ?: super.getChangePayload(oldItem, newItem)
    })

    private val items: MutableList<ListItem>
        get() = differ.currentList

    /**
     * 存储适配器中所有ListItem和Delegate的一一对应关系
     */
    private val bindMaps: MutableList<BindMap> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        bindMaps.first { it.isViewForType(items[position]) }.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        bindMaps.first { it.type == viewType }.delegate.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: VH, position: Int) =
        bindMaps.first { it.type == holder.itemViewType }.delegate
            .onBindViewHolder(holder, items[position], mutableListOf())

    /**
     * 三个参数的onBindViewHolder()方法中可以实现item的局部刷新，避免"白鹳一闪"的问题
     */
    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            bindMaps.first { it.type == holder.itemViewType }.delegate
                .onBindViewHolder(holder, items[position], payloads)
        }
    }

    fun addDelegate(isViewForType: (ListItem) -> Boolean, delegate: Delegate): GAdapter = apply {
        bindMaps.add(BindMap(bindMaps.size, isViewForType, delegate))
    }

    /**
     * 这个方法必须在RecyclerView绑定GAdapter的最后调用，否则不能对RecyclerView设置Adapter
     */
    fun initialize(): GAdapter = apply {
        recyclerView.adapter = this
    }

    /**
     * 刷新列表的方法
     *
     * 注意：
     * 这个方法仅适用于不支持上拉加载的列表中调用，否则Footer的展示会有问题
     * 如果要刷新支持上拉加载的列表中的数据，需要调用GAdapter的扩展方法：refreshWithFooter(list, hasMore)
     *
     * @param dataList 最新的数据列表
     * @param justRefreshFooter 是否仅更新Footer的状态
     *                          如果为true，表示仅更新Footer的状态，此时不会使用dataList，而是直接当前的数据列表currDataList；
     *                          如果为false，表示不仅要更新Footer，还要更新数据列表，此时才会使用dataList
     */
    fun refresh(dataList: MutableList<out ListItem> = mutableListOf(), justRefreshFooter: Boolean = false) {
        val newList = mutableListOf<ListItem>()
        var targetList = dataList
        if (justRefreshFooter) {
            targetList = currDataList
        }
        targetList.forEach { newList.add(it) }
        currDataList = targetList
        if (newList.isNotEmpty() && bindMaps.any { it.delegate is FooterDelegate }) {
            newList.add(FooterVO(status = currFooterStatus))
        }
        differ.submitList(newList)
    }

    /**
     * 更新当前列表中Footer的状态
     */
    fun setFooterStatus(footerStatus: FooterStatus.Status) {
        this.currFooterStatus = footerStatus
    }
}
