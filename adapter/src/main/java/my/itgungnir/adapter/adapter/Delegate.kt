package my.itgungnir.adapter.adapter

import android.view.ViewGroup

/**
 * Delegate，RecyclerView中每一种viewType对应一个Delegate，即这种类型的item由本Delegate负责展示
 * 使用时，创建一个类实现本接口，然后就可以灵活的实现Delegate中的UI展示和交互逻辑了
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface Delegate {

    /**
     * 本Delegate对应的item的layoutId
     */
    fun layoutId(): Int

    /**
     * 对应RecyclerView.Adapter中的onCreateViewHolder()方法
     */
    fun onCreateViewHolder(parent: ViewGroup): VH

    /**
     * 对应RecyclerView.Adapter中的onBindViewHolder()方法
     */
    fun onBindViewHolder(holder: VH, data: ListItem, payloads: MutableList<Any>)
}
