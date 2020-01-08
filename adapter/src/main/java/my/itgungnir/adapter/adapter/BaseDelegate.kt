package my.itgungnir.adapter.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * BaseDelegate是本框架为用户创建的一个Delegate的子类，默认实现了onCreateViewHolder()方法
 *
 * 由于大多数的Delegate不需要在创建时做什么业务逻辑，因此将这个方法封装了起来，避免用户写过多的模版代码
 * 本类还将onBindViewHolder()方法中的数据类型进行了转换，抽取程onRender()方法，用户可以方便的使用泛型中的类型，而不再需要手动强转
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
abstract class BaseDelegate<T : ListItem> :
    Delegate {

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
