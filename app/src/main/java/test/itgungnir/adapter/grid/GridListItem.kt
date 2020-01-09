package test.itgungnir.adapter.grid

import android.os.Bundle
import my.itgungnir.adapter.adapter.ListItem

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-09
 */
data class GridListItem(
    val text: String = ""
) : ListItem {

    override fun isItemSameTo(oldItem: ListItem): Boolean =
        this.text == (oldItem as GridListItem).text

    override fun isContentSameTo(oldItem: ListItem): Boolean =
        isItemSameTo(oldItem)

    override fun getChangePayload(oldItem: ListItem): Bundle? =
        null
}
