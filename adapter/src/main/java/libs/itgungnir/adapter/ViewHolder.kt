package libs.itgungnir.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView.ViewHolder
 *
 * Created by ITGungnir on 2019-10-10
 */
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var onResumeCallback: (ViewHolder, RecyclableItem) -> Unit = { _, _ -> }

    var onPauseCallback: (ViewHolder, RecyclableItem) -> Unit = { _, _ -> }
}
