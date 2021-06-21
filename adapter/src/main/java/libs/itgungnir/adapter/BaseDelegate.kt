package libs.itgungnir.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * BaseDelegate是本框架为用户创建的一个Delegate的子类，默认实现了onCreateViewHolder()方法
 *
 * 由于大多数的Delegate不需要在创建时做什么业务逻辑，因此将这个方法封装了起来，避免用户写过多的模版代码
 * 本类还将onBindViewHolder()方法中的数据类型进行了转换，抽取程onRender()方法，用户可以方便的使用泛型中的类型，而不再需要手动强转
 *
 * Created by ITGungnir on 2019-10-05
 */
abstract class BaseDelegate<T : RecyclableItem> : Delegate {

    override var adapterRef: WeakReference<RecyclerView.Adapter<ViewHolder>>? = null

    override var recyclerViewRef: WeakReference<RecyclerView>? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val viewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId(), parent, false))
        viewHolder.onResumeCallback = { holder, data -> onResume(holder, data) }
        viewHolder.onPauseCallback = { holder, data -> onPause(holder, data) }
        return viewHolder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, data: RecyclableItem, payloads: MutableList<Any>) =
        onRender(holder, data as T, payloads as MutableList<Bundle?>)

    abstract fun onRender(holder: ViewHolder, data: T, payloads: MutableList<Bundle?>)
}
