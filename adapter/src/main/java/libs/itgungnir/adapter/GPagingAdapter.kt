package libs.itgungnir.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class GPagingAdapter : PagingDataAdapter<RecyclableItem, ViewHolder>(itemComparator) {

    companion object {
        private val itemComparator = object : DiffUtil.ItemCallback<RecyclableItem>() {
            override fun areItemsTheSame(oldItem: RecyclableItem, newItem: RecyclableItem): Boolean =
                oldItem.javaClass.name == newItem.javaClass.name && newItem.isItemSameTo(oldItem)

            override fun areContentsTheSame(oldItem: RecyclableItem, newItem: RecyclableItem): Boolean =
                newItem.isContentSameTo(oldItem)

            override fun getChangePayload(oldItem: RecyclableItem, newItem: RecyclableItem): Any? =
                newItem.getChangePayload(oldItem) ?: super.getChangePayload(oldItem, newItem)
        }
    }

    private var recyclerViewRef: WeakReference<RecyclerView>? = null

    /**
     * 存储适配器中所有RecyclableItem和Delegate的一一对应关系
     */
    private val bindMaps: MutableList<BindMap> = mutableListOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef = null
    }

    override fun getItemViewType(position: Int): Int =
        bindMaps.first { it.isItemForType(getItem(position)) }.type

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
        getItem(position)?.let { item ->
            bindMaps.first { it.type == holder.itemViewType }.delegate.onBindViewHolder(holder, item, mutableListOf())
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            getItem(position)?.let { item ->
                bindMaps.first { it.type == holder.itemViewType }.delegate.onBindViewHolder(holder, item, payloads)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val index = holder.bindingAdapterPosition
        if (index < 0 || index >= itemCount) {
            return
        }
        getItem(index)?.let { item ->
            holder.onResumeCallback.invoke(holder, item)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val index = holder.bindingAdapterPosition
        if (index < 0 || index >= itemCount) {
            return
        }
        getItem(index)?.let { item ->
            holder.onPauseCallback.invoke(holder, item)
        }
    }

    fun addDelegate(isViewForType: (RecyclableItem?) -> Boolean, delegate: Delegate): GPagingAdapter = apply {
        bindMaps.add(BindMap(bindMaps.size, isViewForType, delegate))
    }
}
