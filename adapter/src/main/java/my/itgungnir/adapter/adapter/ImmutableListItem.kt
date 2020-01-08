package my.itgungnir.adapter.adapter

import android.os.Bundle
import my.itgungnir.adapter.ListItem

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface ImmutableListItem : ListItem {

    override fun isItemSameTo(oldItem: ListItem): Boolean = true

    override fun isContentSameTo(oldItem: ListItem): Boolean = true

    override fun getChangePayload(oldItem: ListItem): Bundle? = null
}