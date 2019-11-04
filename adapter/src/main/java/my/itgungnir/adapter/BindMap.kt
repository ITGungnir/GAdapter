package my.itgungnir.adapter

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
