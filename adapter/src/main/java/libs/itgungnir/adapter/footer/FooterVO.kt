package libs.itgungnir.adapter.footer

import android.os.Bundle
import libs.itgungnir.adapter.RecyclableItem

/**
 * 上拉加载的Footer展示的VO类
 *
 * 所有的Footer都将使用这个类来做状态的管理
 *
 * Created by ITGungnir on 2020-01-07
 */
data class FooterVO(
    // 当前的状态，默认是IDLE状态
    val status: FooterStatus.Status = FooterStatus.Status.IDLE
) : RecyclableItem {

    /**
     * 每个列表中只有一个Footer的item
     */
    override fun isItemSameTo(oldItem: RecyclableItem): Boolean = true

    /**
     * 当Footer的状态发生变化时，则视为item发生了局部刷新
     */
    override fun isContentSameTo(oldItem: RecyclableItem): Boolean =
        this.status.flag == (oldItem as FooterVO).status.flag

    /**
     * 将最新状态放到payload中用于局部刷新
     */
    override fun getChangePayload(oldItem: RecyclableItem): Bundle =
        Bundle().apply { putInt("PL_FOOTER_STATUS", this@FooterVO.status.flag) }
}
