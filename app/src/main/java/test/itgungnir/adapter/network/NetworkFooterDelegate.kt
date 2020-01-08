package test.itgungnir.adapter.network

import android.view.View
import kotlinx.android.synthetic.main.view_list_footer.view.*
import my.itgungnir.adapter.footer.FooterDelegate
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-07
 */
class NetworkFooterDelegate(private val failRetry: () -> Unit) : FooterDelegate() {

    override fun layoutId(): Int = R.layout.view_list_footer

    override fun onIdle(view: View) {
        view.footer_loading.visibility = View.GONE
        view.footer_text.visibility = View.VISIBLE
        view.footer_text.text = "上拉加载更多数据"
        view.setOnClickListener(null)
    }

    override fun onProgressing(view: View) {
        view.footer_loading.visibility = View.VISIBLE
        view.footer_text.visibility = View.GONE
        view.setOnClickListener(null)
    }

    override fun onNoMore(view: View) {
        view.footer_loading.visibility = View.GONE
        view.footer_text.visibility = View.VISIBLE
        view.footer_text.text = "没有更多数据了"
        view.setOnClickListener(null)
    }

    override fun onFailed(view: View) {
        view.footer_loading.visibility = View.GONE
        view.footer_text.visibility = View.VISIBLE
        view.footer_text.text = "加载失败，点击重试"
        view.setOnClickListener {
            failRetry.invoke()
        }
    }
}