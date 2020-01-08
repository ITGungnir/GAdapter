package test.itgungnir.adapter

import androidx.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-10-05
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this)
    }
}