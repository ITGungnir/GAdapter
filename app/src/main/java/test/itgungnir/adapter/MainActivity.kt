package test.itgungnir.adapter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import test.itgungnir.adapter.multiple.MultipleActivity
import test.itgungnir.adapter.network.NetworkActivity
import test.itgungnir.adapter.network.NetworkWithoutFooterActivity
import test.itgungnir.adapter.payloads.PayloadsActivity
import test.itgungnir.adapter.simple.SimpleActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 简单的RecyclerView
        btn_simple.setOnClickListener {
            start(SimpleActivity::class.java)
        }

        // 增加局部更新功能
        btn_payloads.setOnClickListener {
            start(PayloadsActivity::class.java)
        }

        // 多itemType的展示
        btn_multiple.setOnClickListener {
            start(MultipleActivity::class.java)
        }

        // 网络访问 + 分页
        btn_network.setOnClickListener {
            start(NetworkActivity::class.java)
        }

        // 网络访问，无分页
        btn_network_without_footer.setOnClickListener {
            start(NetworkWithoutFooterActivity::class.java)
        }
    }

    private fun start(clz: Class<out AppCompatActivity>) = startActivity(Intent(this, clz))
}
