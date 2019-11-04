package test.itgungnir.adapter

import androidx.multidex.MultiDexApplication
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.squareup.leakcanary.LeakCanary

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class App : MultiDexApplication() {

    companion object {
        init {
            // 初始化全局的刷新/加载控件的一些属性
            SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
                layout.setEnableAutoLoadMore(false)
                layout.setEnableLoadMoreWhenContentNotFull(false)
            }
            // 设置全局的下拉刷新Header
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> ClassicsHeader(context) }
            // 设置全局的下拉刷新Footer
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(context) }
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}