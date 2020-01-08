package my.itgungnir.adapter

import android.view.ViewGroup
import my.itgungnir.adapter.adapter.VH

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
interface Delegate {

    fun layoutId(): Int

    fun onCreateViewHolder(parent: ViewGroup): VH

    fun onBindViewHolder(holder: VH, data: ListItem, payloads: MutableList<Any>)
}