package test.itgungnir.adapter.ui.delegate

import android.os.Bundle
import android.view.View
import android.widget.TextView
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.RecyclableItem
import libs.itgungnir.adapter.ViewHolder
import libs.itgungnir.adapter.footer.FooterDelegate
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

class NetworkItemFooterDelegate(private val failRetry: () -> Unit) : FooterDelegate() {

    override fun layoutId(): Int = R.layout.delegate_footer

    override fun onIdle(view: View) {
        view.findViewById<View>(R.id.footer_loading)?.visibility = View.GONE
        view.findViewById<TextView>(R.id.footer_text)?.apply {
            visibility = View.VISIBLE
            text = "上拉加载更多数据"
        }
        view.setOnClickListener(null)
    }

    override fun onProgressing(view: View) {
        view.findViewById<View>(R.id.footer_loading)?.visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.footer_text)?.visibility = View.GONE
        view.setOnClickListener(null)
    }

    override fun onNoMore(view: View) {
        view.findViewById<View>(R.id.footer_loading)?.visibility = View.GONE
        view.findViewById<TextView>(R.id.footer_text)?.apply {
            visibility = View.VISIBLE
            text = "没有更多数据了"
        }
        view.setOnClickListener(null)
    }

    override fun onFailed(view: View) {
        view.findViewById<View>(R.id.footer_loading)?.visibility = View.GONE
        view.findViewById<TextView>(R.id.footer_text)?.apply {
            visibility = View.VISIBLE
            text = "加载失败，点击重试"
        }
        view.setOnClickListener {
            failRetry.invoke()
        }
    }
}
