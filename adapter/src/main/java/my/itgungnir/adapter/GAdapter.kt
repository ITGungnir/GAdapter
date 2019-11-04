package my.itgungnir.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class GAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<VH>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
            newItem.isItemSameTo(oldItem)

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

    fun refresh(dataList: MutableList<ListItem>) {
        val newList = mutableListOf<ListItem>()
        dataList.forEach { newList.add(it) }
        differ.submitList(newList)
    }
}