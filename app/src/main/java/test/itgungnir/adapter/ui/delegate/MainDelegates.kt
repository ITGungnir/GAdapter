package test.itgungnir.adapter.ui.delegate

import android.os.Bundle
import android.widget.Button
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.RecyclableItem
import libs.itgungnir.adapter.ViewHolder
import test.itgungnir.adapter.R

data class ButtonBean(
    val title: String,
    val clickCallback: () -> Unit
) : RecyclableItem

class ButtonDelegate : BaseDelegate<ButtonBean>() {
    override fun layoutId(): Int = R.layout.delegate_button
    override fun onRender(holder: ViewHolder, data: ButtonBean, payloads: MutableList<Bundle?>) {
        (holder.itemView as? Button)?.apply {
            text = data.title
            setOnClickListener { data.clickCallback.invoke() }
        }
    }
}
