package libs.itgungnir.adapter.footer

import android.os.Bundle
import android.view.View
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.ViewHolder

/**
 * Footer的Delegate，所有自定义的Footer的Delegate都需要继承自这个类
 *
 * Created by ITGungnir on 2020-01-07
 */
abstract class FooterDelegate : BaseDelegate<FooterVO>() {

    override fun onRender(holder: ViewHolder, data: FooterVO, payloads: MutableList<Bundle?>) {
        var statusFlag = data.status.flag
        // 如果payload不为空，则从payload中取数据
        if (!payloads.isNullOrEmpty()) {
            statusFlag = payloads[0]?.getInt("PL_FOOTER_STATUS") ?: statusFlag
        }
        // 根据Footer的最新状态，渲染Footer的UI
        when (statusFlag) {
            FooterStatus.Status.IDLE.flag -> onIdle(holder.itemView)
            FooterStatus.Status.PROGRESSING.flag -> onProgressing(holder.itemView)
            FooterStatus.Status.NO_MORE.flag -> onNoMore(holder.itemView)
            FooterStatus.Status.FAILED.flag -> onFailed(holder.itemView)
        }
    }

    /**
     * 当Footer的最新状态变为IDLE时将触发本方法
     */
    abstract fun onIdle(view: View)

    /**
     * 当Footer的最新状态变为PROGRESSING时将触发本方法
     */
    abstract fun onProgressing(view: View)

    /**
     * 当Footer的最新状态变为NO_MORE时将触发本方法
     */
    abstract fun onNoMore(view: View)

    /**
     * 当Footer的最新状态变为FAILED时将触发本方法
     */
    abstract fun onFailed(view: View)
}
