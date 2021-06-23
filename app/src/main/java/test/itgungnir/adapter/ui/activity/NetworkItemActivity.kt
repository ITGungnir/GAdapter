package test.itgungnir.adapter.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import libs.itgungnir.adapter.GAdapter
import libs.itgungnir.adapter.footer.FooterStatus
import test.itgungnir.adapter.databinding.ActivityNetworkItemBinding
import test.itgungnir.adapter.ui.delegate.*
import test.itgungnir.adapter.ui.viewmodel.NetworkItemViewModel

class NetworkItemActivity : AppCompatActivity() {

    private val viewModel: NetworkItemViewModel by viewModels()

    private var binding: ActivityNetworkItemBinding? = null

    private var listAdapter: GAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkItemBinding.inflate(layoutInflater).apply { setContentView(root) }
        observeStates()
        initViews()
        viewModel.refreshDataList()
    }

    private fun initViews() {
        binding?.refreshLayout?.let { refreshLayout ->
            refreshLayout.setOnRefreshListener {
                if (listAdapter?.isLoadingMore() == false) {
                    viewModel.refreshDataList()
                }
            }
        }
        binding?.list?.let { rcv ->
            rcv.layoutManager = LinearLayoutManager(this)
            listAdapter = GAdapter()
                .addDelegate({ it is NetworkItemBean }, NetworkItemDelegate())
                .setFooterDelegate(
                    { binding?.refreshLayout?.isRefreshing == false },
                    NetworkItemFooterDelegate(viewModel::loadMoreDataList)
                )
                .onLoadMore { viewModel.loadMoreDataList() }
            rcv.adapter = listAdapter
        }
    }

    private fun observeStates() {
        viewModel.refreshingState.observe(this) {
            binding?.refreshLayout?.isRefreshing = it
        }
        viewModel.firstPageDataState.observe(this) {
            it ?: return@observe
            listAdapter?.refresh(it.second, it.first) { binding?.list?.smoothScrollToPosition(0) }
        }
        viewModel.otherPageDataState.observe(this) {
            it ?: return@observe
            listAdapter?.append(it.second, it.first)
        }
        viewModel.refreshErrorState.observe(this) {
            it ?: return@observe
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
        viewModel.loadMoreErrorState.observe(this) {
            it ?: return@observe
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            listAdapter?.updateFooter(FooterStatus.Status.FAILED)
        }
    }
}
