package test.itgungnir.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View

/**
 * 扩展函数：Activity之间页面跳转
 */
fun Context.nav(targetClass: Class<out Activity>) {
    startActivity(Intent(this, targetClass))
}

/**
 * 扩展函数：判断是否显示View，如果显示则触发回调
 */
fun View.ifShow(ifShow: Boolean, showCallback: (View) -> Unit = {}) {
    if (ifShow) {
        visibility = View.VISIBLE
        showCallback.invoke(this)
    } else {
        visibility = View.GONE
    }
}
