package my.itgungnir.adapter.adapter

import android.os.Bundle

/**
 * GAdapter支持item的布局刷新，但也有一些item是不会改变的，也就不会发生局部刷新
 * 对于这样的item，可以让其对应的VO类实现本接口
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface ImmutableListItem : ListItem {

    /**
     * 判断两个VO是否相同的方法
     * 本方法永远返回true，表示是同一个item
     */
    override fun isItemSameTo(oldItem: ListItem): Boolean = true

    /**
     * 判断是否需要局部更新UI的方法
     * 本方法永远返回true，表示不需要做局部刷新
     */
    override fun isContentSameTo(oldItem: ListItem): Boolean = true

    /**
     * 封装修改量payload的方法
     * 由于isContentSameTo()方法返回true，因此不会走到这个方法
     */
    override fun getChangePayload(oldItem: ListItem): Bundle? = null
}
