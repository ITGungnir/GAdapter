package test.itgungnir.adapter.simple

import my.itgungnir.adapter.adapter.ImmutableListItem

/**
 * Description: 不需要刷新的数据可以直接实现ImmutableListItem接口
 *
 * Created by wangzhiyu1 on 2019-10-01
 */
data class SimpleListItem(val content: String) : ImmutableListItem