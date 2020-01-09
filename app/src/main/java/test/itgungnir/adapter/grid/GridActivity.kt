package test.itgungnir.adapter.grid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
class GridActivity : AppCompatActivity() {

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
            layoutManager = GridLayoutManager(this@GridActivity, 4, GridLayoutManager.VERTICAL, false)
            listAdapter = getGAdapter().addDelegate({ it is GridListItem }, GridDelegate())
                .addFooterDelegate(GridFooterDelegate())
                .initialize()
            setOnLoadMoreListener({ true }) { loadMorePages() }
        }
        // Init data
        loadFirstPage()
    }

    private fun loadFirstPage() {
        for (i in 1..20) {
            dataList.add(GridListItem(text = "List item ${UUID.randomUUID()}"))
        }
        listAdapter.refreshWithFooter(dataList, true)
    }

    @SuppressLint("CheckResult")
    private fun loadMorePages() {
        Observable.timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (i in 1..3) {
                    dataList.add(GridListItem(text = "New List item ${UUID.randomUUID()}"))
                }
                listAdapter.refreshWithFooter(dataList, true)
            }
    }
}
