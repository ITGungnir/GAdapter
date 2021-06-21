package test.itgungnir.adapter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import libs.itgungnir.adapter.GAdapter
import test.itgungnir.adapter.databinding.ActivityMainBinding
import test.itgungnir.adapter.nav
import test.itgungnir.adapter.ui.delegate.ButtonBean
import test.itgungnir.adapter.ui.delegate.ButtonDelegate

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private var listAdapter: GAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        initViews()
    }

    private fun initViews() {
        binding?.list?.let { rcv ->
            rcv.layoutManager = LinearLayoutManager(this)
            listAdapter = GAdapter().addDelegate({ true }, ButtonDelegate())
            rcv.adapter = listAdapter
        }
        listAdapter?.refresh(entrances())
    }

    private fun entrances() = mutableListOf(
        ButtonBean("多类型Item") { nav(MultipleItemActivity::class.java) },
        ButtonBean("数据局部刷新") { nav(PayloadItemActivity::class.java) },
        ButtonBean("网络数据分页加载") { nav(NetworkItemActivity::class.java) }
    )

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
