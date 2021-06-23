package libs.itgungnir.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

/**
 * Delegate，RecyclerView中每一种viewType对应一个Delegate，即这种类型的item由本Delegate负责展示
 * 使用时，创建一个类实现本接口，然后就可以灵活的实现Delegate中的UI展示和交互逻辑了
 *
 * Created by ITGungnir on 2019-10-05
 */
interface Delegate {

    var adapterRef: WeakReference<GAdapter>?

    var recyclerViewRef: WeakReference<RecyclerView>?

    /**
     * 本Delegate对应的item的layoutId
     */
    fun layoutId(): Int

    /**
     * 对应RecyclerView.Adapter中的onCreateViewHolder()方法
     */
    fun onCreateViewHolder(parent: ViewGroup): ViewHolder

    /**
     * 对应RecyclerView.Adapter中的onBindViewHolder()方法
     */
    fun onBindViewHolder(holder: ViewHolder, data: RecyclableItem, payloads: MutableList<Any>)

    /**
     * 当item被滑动进屏幕时回调这个方法
     */
    fun onResume(holder: ViewHolder, data: RecyclableItem) {}

    /**
     * 当item被滑动出屏幕时回调这个方法
     */
    fun onPause(holder: ViewHolder, data: RecyclableItem) {}
}
