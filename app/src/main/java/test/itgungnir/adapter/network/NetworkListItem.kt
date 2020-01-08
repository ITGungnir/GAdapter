package test.itgungnir.adapter.network

import android.os.Bundle
import my.itgungnir.adapter.adapter.ListItem

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-06
 */
data class NetworkListItem(
    val id: Long,
    val title: String,
    val author: String,
    val desc: String
) : ListItem {

    override fun isItemSameTo(oldItem: ListItem): Boolean =
        this.id == (oldItem as NetworkListItem).id

    override fun isContentSameTo(oldItem: ListItem): Boolean =
        this.title == (oldItem as NetworkListItem).title && this.author == oldItem.author && this.desc == oldItem.desc

    override fun getChangePayload(oldItem: ListItem): Bundle? = null
}