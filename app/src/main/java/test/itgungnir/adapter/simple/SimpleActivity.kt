package test.itgungnir.adapter.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_simple.*
import my.itgungnir.adapter.adapter.ListItem
import my.itgungnir.adapter.getGAdapter
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by wangzhiyu1 on 2019-09-28
 */
class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        list.apply {
            layoutManager = LinearLayoutManager(this@SimpleActivity)
            addItemDecoration(DividerItemDecoration(this@SimpleActivity, DividerItemDecoration.VERTICAL))
        }
        // 通过RecyclerView的扩展函数getGAdapter()来获取一个GAdapter的实例
        val listAdapter = list.getGAdapter()
            // 通过addDelegate()方法为RecyclerView添加一个viewType，
            // 第一个参数是一个Lambda表达式，用于提供ListItem的筛选条件；
            // 第二个参数是一个Delegate对象，
            // 即符合第一个参数中的筛选条件的ListItem就使用第二个参数中的Delegate进行渲染
            .addDelegate({ it is SimpleListItem }, SimpleDelegate())
            // 前面的代码都是对GAdapter的配置，最后需要将这个GAdapter绑定到RecyclerView上
            .initialize()

        val dataList = mutableListOf<ListItem>()
        for (i in 1..30) {
            dataList.add(SimpleListItem("List item $i"))
        }
        // 通过refresh()方法刷新列表
        listAdapter.refresh(dataList)
    }
}
