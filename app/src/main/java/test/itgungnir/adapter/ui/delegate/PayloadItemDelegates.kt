package test.itgungnir.adapter.ui.delegate

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import libs.itgungnir.adapter.BaseDelegate
import libs.itgungnir.adapter.RecyclableItem
import libs.itgungnir.adapter.ViewHolder
import test.itgungnir.adapter.R
import test.itgungnir.adapter.ifShow

data class UserInfoDelegateBean(
    val userId: Long,
    val userName: String,
    val userIntroduction: String?,
    val userPhone: String?,
    val userEmail: String?,
    val selectable: Boolean,
    val selected: Boolean
) : RecyclableItem {

    override fun isItemSameTo(oldItem: RecyclableItem): Boolean =
        this.userId == (oldItem as? UserInfoDelegateBean)?.userId

    override fun isContentSameTo(oldItem: RecyclableItem): Boolean =
        this.userName == (oldItem as? UserInfoDelegateBean)?.userName &&
                this.userIntroduction == oldItem.userIntroduction &&
                this.userPhone == oldItem.userPhone &&
                this.userEmail == oldItem.userEmail &&
                this.selectable == oldItem.selectable &&
                this.selected == oldItem.selected

    override fun getChangePayload(oldItem: RecyclableItem): Bundle? {
        if (oldItem !is UserInfoDelegateBean) {
            return null
        }
        var payload: Bundle? = null
        if (this.userName != oldItem.userName) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putString("userName", this.userName)
        }
        if (this.userIntroduction != oldItem.userIntroduction) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putString("userIntroduction", this.userIntroduction)
        }
        if (this.userPhone != oldItem.userPhone) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putString("userPhone", this.userPhone)
        }
        if (this.userEmail != oldItem.userEmail) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putString("userEmail", this.userEmail)
        }
        if (this.selectable != oldItem.selectable) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putBoolean("selectable", this.selectable)
        }
        if (this.selected != oldItem.selected) {
            if (payload == null) {
                payload = Bundle()
            }
            payload.putBoolean("selected", this.selected)
        }
        return payload
    }
}

class UserInfoDelegate : BaseDelegate<UserInfoDelegateBean>() {

    override fun layoutId(): Int = R.layout.delegate_user

    override fun onRender(holder: ViewHolder, data: UserInfoDelegateBean, payloads: MutableList<Bundle?>) {
        val bundle = payloads.firstOrNull { it?.isEmpty == false }
        if (bundle == null) {
            renderUserNam(holder, data.userName)
            renderUserIntroduction(holder, data.userIntroduction)
            renderUserPhone(holder, data.userPhone)
            renderUserEmail(holder, data.userEmail)
            renderSelectable(holder, data.selectable)
            renderSelected(holder, data.selected)
        } else {
            bundle.keySet().forEach { key ->
                when (key) {
                    "userName" -> bundle.getString("userName")?.let {
                        renderUserNam(holder, it)
                    }
                    "userIntroduction" -> bundle.getString("userIntroduction")?.let {
                        renderUserIntroduction(holder, it)
                    }
                    "userPhone" -> bundle.getString("userPhone")?.let {
                        renderUserPhone(holder, it)
                    }
                    "userEmail" -> bundle.getString("userEmail")?.let {
                        renderUserEmail(holder, it)
                    }
                    "selectable" -> renderSelectable(holder, bundle.getBoolean("selectable"))
                    "selected" -> renderSelected(holder, bundle.getBoolean("selected"))
                }
            }
        }
        holder.itemView.findViewById<ImageView>(R.id.checker)?.setOnClickListener {
            val index = holder.bindingAdapterPosition
            adapterRef?.get()
                ?.update(index, { (it as? UserInfoDelegateBean)?.copy(selected = !it.selected) ?: it })
        }
    }

    override fun onResume(holder: ViewHolder, data: RecyclableItem) {
        super.onResume(holder, data)
        Log.e("---->>", "---->>onResume: ${holder.bindingAdapterPosition}")
    }

    override fun onPause(holder: ViewHolder, data: RecyclableItem) {
        super.onPause(holder, data)
        Log.e("---->>", "---->>onPause: ${holder.bindingAdapterPosition}")
    }

    private fun renderUserNam(holder: ViewHolder, userName: String?) {
        holder.itemView.findViewById<TextView>(R.id.userName)?.ifShow(!userName.isNullOrEmpty()) {
            (it as? TextView)?.text = userName
        }
    }

    private fun renderUserIntroduction(holder: ViewHolder, userIntroduction: String?) {
        holder.itemView.findViewById<TextView>(R.id.userIntroduction)?.ifShow(!userIntroduction.isNullOrEmpty()) {
            (it as? TextView)?.text = userIntroduction
        }
    }

    private fun renderUserPhone(holder: ViewHolder, userPhone: String?) {
        holder.itemView.findViewById<TextView>(R.id.userPhone)?.ifShow(!userPhone.isNullOrEmpty()) {
            (it as? TextView)?.text = userPhone
        }
    }

    private fun renderUserEmail(holder: ViewHolder, userEmail: String?) {
        holder.itemView.findViewById<TextView>(R.id.userEmail)?.ifShow(!userEmail.isNullOrEmpty()) {
            (it as? TextView)?.text = userEmail
        }
    }

    private fun renderSelectable(holder: ViewHolder, selectable: Boolean) {
        holder.itemView.findViewById<ImageView>(R.id.checker)?.ifShow(selectable)
    }

    private fun renderSelected(holder: ViewHolder, selected: Boolean) {
        holder.itemView.findViewById<ImageView>(R.id.checker)?.setImageResource(
            when (selected) {
                true -> R.drawable.ic_selected_selected
                else -> R.drawable.ic_selected_normal
            }
        )
    }
}
