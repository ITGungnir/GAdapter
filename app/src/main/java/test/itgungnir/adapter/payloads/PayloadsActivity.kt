package test.itgungnir.adapter.payloads

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_payloads.*
import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.ListItem
import my.itgungnir.adapter.getGAdapter
import test.itgungnir.adapter.R
import java.util.*

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-09-28
 */
class PayloadsActivity : AppCompatActivity() {

    private lateinit var listAdapter: GAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payloads)

        val dataList = mutableListOf<ListItem>()

        list.apply {
            layoutManager = LinearLayoutManager(this@PayloadsActivity)
            addItemDecoration(DividerItemDecoration(this@PayloadsActivity, DividerItemDecoration.VERTICAL))
        }
        listAdapter = list.getGAdapter()
            // PayloadsDelegate中需要传入一个Lambda，即一个回调，在点击前面的复选框时反选这个复选框
            .addDelegate({ it is PayloadsListItem }, PayloadsDelegate { index ->
                val targetItem = dataList[index] as PayloadsListItem
                dataList[index] = targetItem.copy(selected = !targetItem.selected)
                listAdapter.refresh(dataList)
            })
            .initialize()

        for (i in 1..3) {
            dataList.add(PayloadsListItem(UUID.randomUUID().toString()))
        }
        listAdapter.refresh(dataList)

        btn_add.setOnClickListener {
            dataList.add(1, PayloadsListItem(UUID.randomUUID().toString()))
            listAdapter.refresh(dataList)
        }

        btn_remove.setOnClickListener {
            if (dataList.size > 1) {
                dataList.removeAt(1)
                listAdapter.refresh(dataList)
            }
        }
    }
}
