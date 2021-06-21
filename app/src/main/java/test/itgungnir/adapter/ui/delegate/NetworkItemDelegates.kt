package test.itgungnir.adapter.ui.delegate

import android.os.Bundle
import android.widget.TextView
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.RecyclableItem
import libs.itgungnir.adapter.ViewHolder
import test.itgungnir.adapter.R

data class NetworkItemBean(
    val id: Long,
    val title: String
) : RecyclableItem {
    override fun isItemSameTo(oldItem: RecyclableItem): Boolean = this.id == (oldItem as? NetworkItemBean)?.id
}

class NetworkItemDelegate : BaseDelegate<NetworkItemBean>() {
    override fun layoutId(): Int = R.layout.delegate_network_item
    override fun onRender(holder: ViewHolder, data: NetworkItemBean, payloads: MutableList<Bundle?>) {
        holder.itemView.findViewById<TextView>(R.id.articleTitle)?.text = data.title
    }
}

data class NetworkDividerBean(
    val id: Byte = 0
) : RecyclableItem

class NetworkDividerDelegate : BaseDelegate<NetworkDividerBean>() {
    override fun layoutId(): Int = R.layout.delegate_network_divider
    override fun onRender(holder: ViewHolder, data: NetworkDividerBean, payloads: MutableList<Bundle?>) {}
}
