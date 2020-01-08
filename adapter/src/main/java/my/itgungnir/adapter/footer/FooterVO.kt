package my.itgungnir.adapter.footer

import android.os.Bundle
import my.itgungnir.adapter.adapter.ListItem
import my.itgungnir.adapter.PL_FOOTER_STATUS

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
) : ListItem {

    /**
     * 每个列表中只有一个Footer的item
     */
    override fun isItemSameTo(oldItem: ListItem): Boolean = true

    /**
     * 当Footer的状态发生变化时，则视为item发生了局部刷新
     */
    override fun isContentSameTo(oldItem: ListItem): Boolean = this.status.flag == (oldItem as FooterVO).status.flag

    /**
     * 将最新状态放到payload中用于局部刷新
     */
    override fun getChangePayload(oldItem: ListItem): Bundle =
        Bundle().apply { putInt(PL_FOOTER_STATUS, this@FooterVO.status.flag) }
}
