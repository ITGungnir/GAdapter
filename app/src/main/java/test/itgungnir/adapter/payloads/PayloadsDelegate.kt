package test.itgungnir.adapter.payloads

import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.item_payloads.view.*
import my.itgungnir.adapter.BaseDelegate
import my.itgungnir.adapter.adapter.VH
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class PayloadsDelegate(private val callback1: (Int) -> Unit) : BaseDelegate<PayloadsListItem>() {

    override fun layoutId(): Int = R.layout.item_payloads

    override fun onRender(holder: VH, data: PayloadsListItem, payloads: MutableList<Bundle?>) {
        holder.itemView.apply {
            // 如果payloads不为空，则从中取出新的局部数据，并更新局部控件，以此达到局部更新的目的，减少UI重绘，提升性能
            if (payloads.isNotEmpty()) {
                payloads[0]?.let { payload ->
                    payload.keySet().forEach { key ->
                        when (key) {
                            "PL_SELECT" -> iv_checker.setImageResource(getCheckerRes(payload.getBoolean(key)))
                        }
                    }
                }
            } else {
                // 如果payloads为空，则说明这是一条新数据，直接渲染UI即可
                iv_checker.apply {
                    setImageResource(getCheckerRes(data.selected))
                    setOnClickListener {
                        if (holder.adapterPosition == -1) {
                            return@setOnClickListener
                        }
                        callback1.invoke(holder.adapterPosition)
                    }
                }

                tv_content.apply {
                    text = data.content
                    setOnClickListener {
                        if (holder.adapterPosition == -1) {
                            return@setOnClickListener
                        }
                        Toast.makeText(context, "点击：${data.content}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getCheckerRes(selected: Boolean) = when (selected) {
        true -> R.drawable.svg_selected_selected
        else -> R.drawable.svg_selected_normal
    }
}