package libs.itgungnir.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
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

    fun refresh(dataList: MutableList<out RecyclableItem>, commitCallback: () -> Unit = {}) {
        val newList = mutableListOf<RecyclableItem>()
        dataList.forEach { newList.add(it) }
        currItems.clear()
        currItems.addAll(newList)
        differ.submitList(newList, commitCallback)
    }

    fun applyAll(applyLambda: (RecyclableItem) -> RecyclableItem, commitCallback: () -> Unit = {}) {
        val newList = currItems.map { applyLambda.invoke(it) }
        refresh(newList.toMutableList(), commitCallback)
    }

    fun insert(index: Int = items.size, data: MutableList<out RecyclableItem>, commitCallback: () -> Unit = {}) {
        if (index < 0 || index > currItems.size) {
            return
        }
        currItems.addAll(index, data)
        refresh(currItems, commitCallback)
    }

    fun append(data: MutableList<out RecyclableItem>, commitCallback: () -> Unit = {}) {
        currItems.addAll(data)
        refresh(currItems, commitCallback)
    }

    fun update(index: Int, updateLambda: (RecyclableItem) -> RecyclableItem, commitCallback: () -> Unit = {}) {
        if (index < 0 || index >= currItems.size) {
            return
        }
        currItems[index] = updateLambda.invoke(currItems[index])
        refresh(currItems, commitCallback)
    }

    fun remove(index: Int, commitCallback: () -> Unit = {}) {
        if (index < 0 || index >= currItems.size) {
            return
        }
        currItems.removeAt(index)
        refresh(currItems, commitCallback)
    }

    fun removeAll(data: MutableList<out RecyclableItem>, commitCallback: () -> Unit = {}) {
        currItems.removeAll(data)
        refresh(currItems, commitCallback)
    }
}
