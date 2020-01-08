package my.itgungnir.adapter.adapter

import my.itgungnir.adapter.Delegate
import my.itgungnir.adapter.ListItem

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
data class BindMap(
    val type: Int,
    val isViewForType: (data: ListItem) -> Boolean,
    val delegate: Delegate
)
