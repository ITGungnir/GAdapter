package my.itgungnir.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import my.itgungnir.adapter.adapter.VH

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
abstract class BaseDelegate<T : ListItem> : Delegate {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup): VH =
        VH(
            LayoutInflater.from(parent.context).inflate(
                layoutId(),
                parent,
                false
            )
        )

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: VH, data: ListItem, payloads: MutableList<Any>) =
        onRender(holder, data as T, payloads as MutableList<Bundle?>)

    abstract fun onRender(holder: VH, data: T, payloads: MutableList<Bundle?>)
}