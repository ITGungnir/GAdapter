package my.itgungnir.adapter.footer

import android.os.Bundle
import my.itgungnir.adapter.ListItem
import my.itgungnir.adapter.PL_FOOTER_STATUS

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-07
 */
data class FooterVO(
    val status: FooterStatus.Status = FooterStatus.Status.IDLE
) : ListItem {

    override fun isItemSameTo(oldItem: ListItem): Boolean = true

    override fun isContentSameTo(oldItem: ListItem): Boolean = this.status.flag == (oldItem as FooterVO).status.flag

    override fun getChangePayload(oldItem: ListItem): Bundle =
        Bundle().apply { putInt(PL_FOOTER_STATUS, this@FooterVO.status.flag) }
}
