package my.itgungnir.adapter.footer

import android.os.Bundle
import android.view.View
import my.itgungnir.adapter.BaseDelegate
import my.itgungnir.adapter.PL_FOOTER_STATUS
import my.itgungnir.adapter.adapter.VH

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-07
 */
abstract class FooterDelegate : BaseDelegate<FooterVO>() {

    override fun onRender(holder: VH, data: FooterVO, payloads: MutableList<Bundle?>) {
        var statusFlag = data.status.flag
        if (!payloads.isNullOrEmpty()) {
            statusFlag = payloads[0]?.getInt(PL_FOOTER_STATUS) ?: statusFlag
        }
        when (statusFlag) {
            FooterStatus.Status.IDLE.flag -> onDefault(holder.itemView)
            FooterStatus.Status.PROGRESSING.flag -> onLoading(holder.itemView)
            FooterStatus.Status.NO_MORE.flag -> onNoMore(holder.itemView)
            FooterStatus.Status.FAILED.flag -> onFailure(holder.itemView)
        }
    }

    abstract fun onDefault(view: View)

    abstract fun onLoading(view: View)

    abstract fun onNoMore(view: View)

    abstract fun onFailure(view: View)
}
