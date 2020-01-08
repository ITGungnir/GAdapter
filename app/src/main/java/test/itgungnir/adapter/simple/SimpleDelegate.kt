package test.itgungnir.adapter.simple

import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.item_simple.view.*
import my.itgungnir.adapter.BaseDelegate
import my.itgungnir.adapter.adapter.VH
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-01
 */
class SimpleDelegate : BaseDelegate<SimpleListItem>() {

    override fun layoutId(): Int = R.layout.item_simple

    override fun onRender(holder: VH, data: SimpleListItem, payloads: MutableList<Bundle?>) {
        holder.itemView.tv_content.apply {
            text = data.content
            setOnClickListener {
                if (holder.adapterPosition == -1) {
                    return@setOnClickListener
                }
                Toast.makeText(context, data.content, Toast.LENGTH_SHORT).show()
            }
        }
    }
}