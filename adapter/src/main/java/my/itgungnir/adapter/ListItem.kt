package my.itgungnir.adapter

import android.os.Bundle

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface ListItem {

    fun isItemSameTo(oldItem: ListItem): Boolean

    fun isContentSameTo(oldItem: ListItem): Boolean

    fun getChangePayload(oldItem: ListItem): Bundle?
}