package test.itgungnir.adapter.payloads

import android.os.Bundle
import my.itgungnir.adapter.ListItem

/**
 * Description: 需要局部刷新的数据需要实现ListItem接口并实现其三个方法
 *
 * DiffUtil的工作机制：
 * 存储着两个列表，分别是旧数据列表和新数据列表。当有新列表被赋值到DiffUtil上时，
 * 会通过排列组合的方式两两比较数据，具体的规则如下：
 * 1. 当areItemsTheSame()方法返回true时，表示两条数据是同一条数据，此时会进入下一步；
 * 2. 当areContentsTheSame()方法返回false时，表示两条数据虽然是同一条数据，但其细节内容
 *    并不相同，此时会进入下一步；
 * 3. getChangePayload()方法会返回新老数据之间的变化的payload，通过这个payload可以局部刷新
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
data class PayloadsListItem(val content: String, var selected: Boolean = false) : ListItem {

    /**
     * 判断新老数据是否是同一条数据
     */
    override fun isItemSameTo(oldItem: ListItem): Boolean =
        this.content == (oldItem as PayloadsListItem).content

    /**
     * 如果新老数据是同一条数据，则判断是否需要局部刷新
     */
    override fun isContentSameTo(oldItem: ListItem): Boolean =
        this.selected == (oldItem as PayloadsListItem).selected

    /**
     * 如果需要局部刷新，则生成局部刷新的payload
     */
    override fun getChangePayload(oldItem: ListItem): Bundle? = if (isContentSameTo(oldItem)) {
        null
    } else {
        Bundle().apply { putBoolean("PL_SELECT", selected) }
    }
}