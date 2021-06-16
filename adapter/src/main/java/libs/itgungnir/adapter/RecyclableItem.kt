package libs.itgungnir.adapter

import android.os.Bundle

/**
 * RecyclableItem，用于绑定到Delegate的实体类的基类，用于在DiffUtil中决定局部/整体刷新的规则
 *
 * Created by ITGungnir on 2019-10-05
 */
interface RecyclableItem {

    /**
     * 判断当前实体类与`oldItem`所对应的是否是同一个RecyclerView的item
     *
     * 如果这个方法返回false，则表示当前实体类和`oldItem`对应的不是同一个item；
     * 如果这个方法返回true，则表示当前实体类和`oldItem`对应的是同一个item，此时需要调用下面的方法，判断是否需要局部刷新
     */
    fun isItemSameTo(oldItem: RecyclableItem): Boolean = false

    /**
     * 判断当前实体类相对于`oldItem`需不需要做局部刷新
     *
     * 这个方法只有在`isItemSameTo`方法返回true时才会被调用
     *
     * 如果这个方法返回true，则说明此item不需要做局部刷新；
     * 如果当前方法返回false，则说明此item需要做局部刷新，此时才会走到`getChangePayload`方法
     */
    fun isContentSameTo(oldItem: RecyclableItem): Boolean = false

    /**
     * 获取当前实体类（newItem）相对于oldItem的变化量
     *
     * 这个方法只有在`isContentSameTo`方法返回false时才会调用
     *
     * 在这个方法中，需要对newItem相对于oldItem所产生的、会影响界面布局的字段收集到一个Bundle对象中
     * 这个Bundle对象会被回调到RecyclerView.Adapter的三个参数的onBindViewHolder方法中进行局部更新
     * 这样做的原因是：DiffUtil会让RecyclerView的数据变化通过动画的方式表现出来，但当发生局部更新时，往往会有"白光一闪"的问题，
     * 而通过payload的方法，可以有效的避免这个问题
     */
    fun getChangePayload(oldItem: RecyclableItem): Bundle? = null
}
