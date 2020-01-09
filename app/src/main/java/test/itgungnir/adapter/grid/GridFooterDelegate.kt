package test.itgungnir.adapter.grid

import android.view.View
import my.itgungnir.adapter.footer.FooterDelegate
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-09
 */
class GridFooterDelegate : FooterDelegate() {

    override fun layoutId(): Int = R.layout.view_list_footer

    override fun onIdle(view: View) {
    }

    override fun onProgressing(view: View) {
    }

    override fun onNoMore(view: View) {
    }

    override fun onFailed(view: View) {
    }
}
