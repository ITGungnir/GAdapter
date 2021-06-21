package test.itgungnir.adapter.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import libs.itgungnir.adapter.GPagingAdapter
import test.itgungnir.adapter.databinding.ActivityNetworkItemBinding
import test.itgungnir.adapter.ui.delegate.NetworkDividerBean
import test.itgungnir.adapter.ui.delegate.NetworkDividerDelegate
import test.itgungnir.adapter.ui.delegate.NetworkItemBean
import test.itgungnir.adapter.ui.delegate.NetworkItemDelegate
import test.itgungnir.adapter.ui.viewmodel.NetworkItemViewModel

class NetworkItemActivity : AppCompatActivity() {

    private val viewModel: NetworkItemViewModel by viewModels()

    private var binding: ActivityNetworkItemBinding? = null

    private var listAdapter: GPagingAdapter? = null

    private var getListJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkItemBinding.inflate(layoutInflater).apply { setContentView(root) }
        initViews()
    }

    private fun initViews() {
        binding?.refreshLayout?.let { refreshLayout ->
            refreshLayout.setOnRefreshListener {
                listAdapter?.refresh()
            }
        }
        binding?.list?.let { rcv ->
            rcv.layoutManager = LinearLayoutManager(this)
            listAdapter = GPagingAdapter()
                .addDelegate({ it is NetworkItemBean }, NetworkItemDelegate())
                .addDelegate({ it is NetworkDividerBean }, NetworkDividerDelegate())
            rcv.adapter = listAdapter
        }
        getNetworkItemList()
        lifecycleScope.launchWhenCreated {
            listAdapter?.loadStateFlow?.collectLatest {
                if (it.refresh !is LoadState.Loading) {
                    binding?.refreshLayout?.isRefreshing = false
                }
            }
        }
    }

    private fun getNetworkItemList(refresh: Boolean = false) {
        getListJob?.cancel()
        getListJob = lifecycleScope.launch {
            viewModel.getNetworkItemList().collect {
                listAdapter?.submitData(it)
                if (refresh) {
                    binding?.refreshLayout?.isRefreshing = false
                }
            }
        }
    }
}
