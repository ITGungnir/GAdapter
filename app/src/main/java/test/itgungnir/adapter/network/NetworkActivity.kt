package test.itgungnir.adapter.network

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_network.*
import my.itgungnir.adapter.*
import my.itgungnir.adapter.adapter.GAdapter
import test.itgungnir.adapter.R
import test.itgungnir.adapter.network.network.NetService
import test.itgungnir.adapter.network.network.NetUtil

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-09-28
 */
@SuppressLint("CheckResult")
class NetworkActivity : AppCompatActivity() {

    private var currPageNo = 0

    private var hasMore = true

    private var dataList = listOf<NetworkListItem>()

    private lateinit var listAdapter: GAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        initViews()
    }

    private fun initViews() {
        // SwipeRefreshLayout
        refreshLayout.setOnRefreshListener {
            refreshDataList()
        }
        // RecyclerView
        list.apply {
            layoutManager = LinearLayoutManager(this@NetworkActivity)
            addItemDecoration(DividerItemDecoration(this@NetworkActivity, DividerItemDecoration.VERTICAL))
            setOnLoadMoreListener({ !refreshLayout.isRefreshing }) { loadMoreDataList() }
        }
        listAdapter = list.getGAdapter()
            .addDelegate({ it is NetworkListItem }, NetworkDelegate())
            .addFooterDelegate(NetworkFooterDelegate {
                listAdapter.loadMoreRetry { loadMoreDataList() }
            })
            .initialize()
        // Init data
        refreshDataList()
    }

    private fun refreshDataList() {
        currPageNo = 0
        NetUtil.withService(NetService::class.java)
            .getArticleList(currPageNo, 60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { refreshLayout.isRefreshing = true }
            .doFinally { refreshLayout.isRefreshing = false }
            .map { it.data.datas.map { item -> NetworkListItem(item.id, item.title, item.author, item.desc) } }
            .subscribe({
                dataList = it
                listAdapter.refreshWithFooter(dataList.toMutableList(), true)
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
    }

    private fun loadMoreDataList() {
        if (refreshLayout.isRefreshing) {
            return
        }
        currPageNo++
        NetUtil.withService(NetService::class.java)
            .getArticleList(currPageNo, 60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                hasMore = !it.data.over
                it
            }
            .map { it.data.datas.map { item -> NetworkListItem(item.id, item.title, item.author, item.desc) } }
            .subscribe({
                dataList = dataList + it
                listAdapter.refreshWithFooter(dataList.toMutableList(), hasMore)
            }, {
                currPageNo--
                listAdapter.loadMoreError()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
    }
}
