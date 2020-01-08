package my.itgungnir.adapter.adapter

/**
 * BindMap实体类
 *
 * 这个实体类用于标记ListItem和Delegate的一一对应关系
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
data class BindMap(
    val type: Int,
    val isViewForType: (data: ListItem) -> Boolean,
    val delegate: Delegate
)
