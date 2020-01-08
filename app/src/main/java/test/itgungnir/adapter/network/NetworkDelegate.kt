package test.itgungnir.adapter.network

import android.os.Bundle
import kotlinx.android.synthetic.main.item_network.view.*
import my.itgungnir.adapter.adapter.BaseDelegate
import my.itgungnir.adapter.adapter.VH
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-06
 */
class NetworkDelegate : BaseDelegate<NetworkListItem>() {

    override fun layoutId(): Int = R.layout.item_network

    override fun onRender(holder: VH, data: NetworkListItem, payloads: MutableList<Bundle?>) {
        holder.itemView.apply {
            tv_author.text = data.author
            tv_title.text = data.title
            tv_desc.text = data.desc
        }
    }
}