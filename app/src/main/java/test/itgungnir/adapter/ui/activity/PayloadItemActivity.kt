package test.itgungnir.adapter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import libs.itgungnir.adapter.GAdapter
import test.itgungnir.adapter.databinding.ActivityPayloadItemBinding
import test.itgungnir.adapter.ifShow
import test.itgungnir.adapter.ui.delegate.UserInfoDelegate
import test.itgungnir.adapter.ui.delegate.UserInfoDelegateBean

class PayloadItemActivity : AppCompatActivity() {

    private var binding: ActivityPayloadItemBinding? = null

    private var listAdapter: GAdapter? = null

    private var nextUserId: Long = 11L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayloadItemBinding.inflate(layoutInflater).apply { setContentView(root) }
        initViews()
    }

    private fun initViews() {
        binding?.list?.let { rcv ->
            rcv.layoutManager = LinearLayoutManager(this)
            listAdapter = GAdapter().addDelegate({ true }, UserInfoDelegate())
            rcv.adapter = listAdapter
        }
        listAdapter?.refresh(initialUserInfoList())
        binding?.operation?.apply {
            text = "管理"
            setOnClickListener {
                if (text == "管理") {
                    text = "完成"
                    binding?.add?.ifShow(false)
                    binding?.delete?.ifShow(true)
                    listAdapter?.applyAll({ (it as? UserInfoDelegateBean)?.copy(selectable = true) ?: it })
                } else {
                    text = "管理"
                    binding?.add?.ifShow(true)
                    binding?.delete?.ifShow(false)
                    listAdapter?.applyAll({ (it as? UserInfoDelegateBean)?.copy(selectable = false) ?: it })
                }
            }
        }
        binding?.add?.setOnClickListener {
            val newUserInfo = UserInfoDelegateBean(
                userId = nextUserId,
                userName = "测试用户姓名$nextUserId",
                userIntroduction = "测试用户信息测试用户信息测试用户信息测试用户信息测试用户信息测试用户信息$nextUserId",
                userPhone = "+86 15477659987 ($nextUserId)",
                userEmail = "testUser$nextUserId@jd.com",
                selectable = false,
                selected = false
            )
            listAdapter?.insert(0, mutableListOf(newUserInfo)) {
                binding?.list?.smoothScrollToPosition(0)
            }
            nextUserId++
        }
        binding?.delete?.setOnClickListener {
            listAdapter?.currItems?.filter { (it as? UserInfoDelegateBean)?.selected == true }
                ?.takeIf { it.isNotEmpty() }
                ?.let {
                    listAdapter?.removeAll(it.toMutableList())
                }
        }
    }

    private fun initialUserInfoList(): MutableList<UserInfoDelegateBean> {
        val userInfoList = mutableListOf<UserInfoDelegateBean>()
        for (i in 1..10) {
            val userInfo = UserInfoDelegateBean(
                userId = i.toLong(),
                userName = "测试用户姓名$i",
                userIntroduction = "测试用户信息测试用户信息测试用户信息测试用户信息测试用户信息测试用户信息$i",
                userPhone = "+86 15477659987 ($i)",
                userEmail = "testUser$i@jd.com",
                selectable = false,
                selected = false
            )
            userInfoList.add(userInfo)
        }
        return userInfoList
    }
}
