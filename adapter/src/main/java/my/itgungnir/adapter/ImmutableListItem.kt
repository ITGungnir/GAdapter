package my.itgungnir.adapter

import android.os.Bundle

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