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
import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.getGAdapter
import test.itgungnir.adapter.R
import test.itgungnir.adapter.network.network.NetService
import test.itgungnir.adapter.network.network.NetUtil

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-08
 */
class NetworkWithoutFooterActivity : AppCompatActivity() {

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
            layoutManager = LinearLayoutManager(this@NetworkWithoutFooterActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@NetworkWithoutFooterActivity, DividerItemDecoration.VERTICAL))
        }
        listAdapter = list.getGAdapter()
            .addDelegate({ it is NetworkListItem }, NetworkDelegate())
            .initialize()
        // Init data
        refreshDataList()
    }

    @SuppressLint("CheckResult")
    private fun refreshDataList() {
        NetUtil.withService(NetService::class.java)
            .getArticleList(0, 60)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { refreshLayout.isRefreshing = true }
            .doFinally { refreshLayout.isRefreshing = false }
            .map { it.data.datas.map { item -> NetworkListItem(item.id, item.title, item.author, item.desc) } }
            .subscribe({
                listAdapter.refresh(it.toMutableList())
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
    }
}