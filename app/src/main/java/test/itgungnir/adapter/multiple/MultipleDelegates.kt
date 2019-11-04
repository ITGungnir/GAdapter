package test.itgungnir.adapter.multiple

import android.os.Bundle
import my.itgungnir.adapter.BaseDelegate
import my.itgungnir.adapter.VH
import test.itgungnir.adapter.R

class MultipleSearchBarDelegate : BaseDelegate<MultipleSearchBar>() {

    override fun layoutId(): Int = R.layout.item_multiple_search

    override fun onRender(holder: VH, data: MultipleSearchBar, payloads: MutableList<Bundle?>) {}
}

class MultipleBannerDelegate : BaseDelegate<MultipleBanner>() {

    override fun layoutId(): Int = R.layout.item_multiple_banner

    override fun onRender(holder: VH, data: MultipleBanner, payloads: MutableList<Bundle?>) {}
}

class MultipleHotBarDelegate : BaseDelegate<MultipleHotBar>() {

    override fun layoutId(): Int = R.layout.item_multiple_hot

    override fun onRender(holder: VH, data: MultipleHotBar, payloads: MutableList<Bundle?>) {}
}

class MultipleDataDelegate : BaseDelegate<MultipleData>() {

    override fun layoutId(): Int = R.layout.item_multiple_data

    override fun onRender(holder: VH, data: MultipleData, payloads: MutableList<Bundle?>) {}
}