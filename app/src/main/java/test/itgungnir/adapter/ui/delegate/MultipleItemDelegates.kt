package test.itgungnir.adapter.ui.delegate

import android.os.Bundle
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.RecyclableItem
import libs.itgungnir.adapter.ViewHolder
import test.itgungnir.adapter.R

data class MultipleItemBean1(
    val id: Byte = 0
) : RecyclableItem

class MultipleItemDelegate1 : BaseDelegate<MultipleItemBean1>() {
    override fun layoutId(): Int = R.layout.delegate_multiple_type1
    override fun onRender(holder: ViewHolder, data: MultipleItemBean1, payloads: MutableList<Bundle?>) {
    }
}

data class MultipleItemBean2(
    val id: Byte = 0
) : RecyclableItem

class MultipleItemDelegate2 : BaseDelegate<MultipleItemBean2>() {
    override fun layoutId(): Int = R.layout.delegate_multiple_type2
    override fun onRender(holder: ViewHolder, data: MultipleItemBean2, payloads: MutableList<Bundle?>) {
    }
}
