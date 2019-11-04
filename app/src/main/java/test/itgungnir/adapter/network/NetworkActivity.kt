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
import my.itgungnir.adapter.GAdapter
import my.itgungnir.adapter.getGAdapter
import test.itgungnir.adapter.R
import test.itgungnir.adapter.network.network.NetService
import test.itgungnir.adapter.network.network.NetUtil

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-09-28
 */
class NetworkActivity : AppCompatActivity() {

    private var currPageNo = 0

    private var dataList = listOf<NetworkListItem>()

    private lateinit var listAdapter: GAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        refreshLayout.setOnRefreshListener {
            currPageNo = 0
            loadDataFromNet()
        }
        refreshLayout.setOnLoadMoreListener {
            currPageNo++
            loadDataFromNet()
        }

        list.apply {
            layoutManager = LinearLayoutManager(this@NetworkActivity)
            addItemDecoration(DividerItemDecoration(this@NetworkActivity, DividerItemDecoration.VERTICAL))
        }
        listAdapter = list.getGAdapter()
            .addDelegate({ it is NetworkListItem }, NetworkDelegate())
            .initialize()

        loadDataFromNet()
    }

    @SuppressLint("CheckResult")
    private fun loadDataFromNet() {
        NetUtil.withService(NetService::class.java)
            .getArticleList(currPageNo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.data.datas.map { item -> NetworkListItem(item.id, item.title, item.author, item.desc) } }
            .subscribe({
                if (currPageNo == 0) {
                    dataList = it
                    listAdapter.refresh(dataList.toMutableList())
                    refreshLayout.finishRefresh()
                } else {
                    dataList = dataList + it
                    listAdapter.refresh(dataList.toMutableList())
                    refreshLayout.finishLoadMore()
                }
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if (currPageNo == 0) {
                    refreshLayout.finishRefresh(false)
                } else {
                    refreshLayout.finishLoadMore(false)
                }
            })
    }
}
