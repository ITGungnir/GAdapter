package test.itgungnir.adapter.multiple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_multiple.*
import my.itgungnir.adapter.adapter.ListItem
import my.itgungnir.adapter.getGAdapter
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-09-28
 */
class MultipleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple)

        list.apply {
            layoutManager = LinearLayoutManager(this@MultipleActivity)
        }
        val listAdapter = list.getGAdapter()
            .addDelegate({ it is MultipleSearchBar }, MultipleSearchBarDelegate())
            .addDelegate({ it is MultipleBanner }, MultipleBannerDelegate())
            .addDelegate({ it is MultipleHotBar }, MultipleHotBarDelegate())
            .addDelegate({ it is MultipleData }, MultipleDataDelegate())
            .initialize()

        val dataList = mutableListOf<ListItem>(MultipleSearchBar, MultipleBanner, MultipleHotBar)
        for (i in 1..5) {
            dataList.add(MultipleData)
        }
        listAdapter.refresh(dataList)
    }
}
