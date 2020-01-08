package my.itgungnir.adapter.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import my.itgungnir.adapter.Delegate
import my.itgungnir.adapter.ListItem
import my.itgungnir.adapter.footer.FooterDelegate
import my.itgungnir.adapter.footer.FooterStatus
import my.itgungnir.adapter.footer.FooterVO

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class GAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<VH>() {

    var currFooterStatus: FooterStatus.Status = FooterStatus.Status.IDLE

    private var currDataList: MutableList<ListItem> = mutableListOf()

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            oldItem.javaClass.name == newItem.javaClass.name && newItem.isItemSameTo(oldItem)

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            newItem.isContentSameTo(oldItem)

        override fun getChangePayload(oldItem: ListItem, newItem: ListItem): Any? =
            newItem.getChangePayload(oldItem) ?: super.getChangePayload(oldItem, newItem)
    })

    private val items: MutableList<ListItem>
        get() = differ.currentList

    private val bindMaps: MutableList<BindMap> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        bindMaps.first { it.isViewForType(items[position]) }.type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        bindMaps.first { it.type == viewType }.delegate.onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: VH, position: Int) =
        bindMaps.first { it.type == holder.itemViewType }.delegate
            .onBindViewHolder(holder, items[position], mutableListOf())

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

    fun initialize(): GAdapter = apply {
        recyclerView.adapter = this
    }

    fun refresh(dataList: MutableList<ListItem> = mutableListOf(), justRefreshFooter: Boolean = false) {
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

    fun setFooterStatus(footerStatus: FooterStatus.Status) {
        this.currFooterStatus = footerStatus
    }
}