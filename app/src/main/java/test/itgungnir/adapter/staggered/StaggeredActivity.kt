package test.itgungnir.adapter.staggered

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_multiple.*
import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.adapter.ListItem
import my.itgungnir.adapter.addFooterDelegate
import my.itgungnir.adapter.getGAdapter
import my.itgungnir.adapter.refreshWithFooter
import my.itgungnir.adapter.setOnLoadMoreListener
import test.itgungnir.adapter.R
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-09
 */
class StaggeredActivity : AppCompatActivity() {

    private lateinit var listAdapter: GAdapter

    private val dataList: MutableList<ListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple)
        initViews()
    }

    private fun initViews() {
        // RecyclerView
        list.apply {
            layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            listAdapter = getGAdapter().addDelegate({ it is StaggeredListItem }, StaggeredDelegate())
                .addFooterDelegate(StaggeredFooterDelegate())
                .initialize()
            setOnLoadMoreListener({ true }) { loadMorePages() }
        }
        // Init data
        loadFirstPage()
    }

    private fun loadFirstPage() {
        for (i in 1..20) {
            if (i % 3 == 0) {
                dataList.add(StaggeredListItem(text = "${UUID.randomUUID()}${UUID.randomUUID()}"))
            } else {
                dataList.add(StaggeredListItem(text = "List item ${UUID.randomUUID()}"))
            }
        }
        listAdapter.refreshWithFooter(dataList, true)
    }

    @SuppressLint("CheckResult")
    private fun loadMorePages() {
        Observable.timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (i in 1..2) {
                    dataList.add(StaggeredListItem(text = "New List item ${UUID.randomUUID()}"))
                }
                dataList.add(StaggeredListItem(text = "${UUID.randomUUID()}${UUID.randomUUID()}"))
                listAdapter.refreshWithFooter(dataList, true)
            }
    }
}
