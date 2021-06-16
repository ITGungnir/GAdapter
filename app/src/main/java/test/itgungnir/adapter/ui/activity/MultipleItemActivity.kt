package test.itgungnir.adapter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import libs.itgungnir.adapter.GAdapter
import test.itgungnir.adapter.databinding.ActivityMultipleItemBinding
import test.itgungnir.adapter.ui.delegate.MultipleItemBean1
import test.itgungnir.adapter.ui.delegate.MultipleItemBean2
import test.itgungnir.adapter.ui.delegate.MultipleItemDelegate1
import test.itgungnir.adapter.ui.delegate.MultipleItemDelegate2

class MultipleItemActivity : AppCompatActivity() {

    private var binding: ActivityMultipleItemBinding? = null

    private var listAdapter: GAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultipleItemBinding.inflate(layoutInflater).apply { setContentView(root) }
        initViews()
    }

    private fun initViews() {
        binding?.list?.let { rcv ->
            rcv.layoutManager = LinearLayoutManager(this)
            listAdapter = GAdapter()
                .addDelegate({ it is MultipleItemBean1 }, MultipleItemDelegate1())
                .addDelegate({ it is MultipleItemBean2 }, MultipleItemDelegate2())
            rcv.adapter = listAdapter
        }
        listAdapter?.refresh(floors())
    }

    private fun floors() = mutableListOf(
        MultipleItemBean1(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean1(),
        MultipleItemBean2(),
        MultipleItemBean1(),
        MultipleItemBean1(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2(),
        MultipleItemBean2()
    )

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
