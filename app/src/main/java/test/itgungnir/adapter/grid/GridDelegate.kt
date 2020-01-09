package test.itgungnir.adapter.grid

import android.os.Bundle
import android.widget.TextView
import my.itgungnir.adapter.adapter.BaseDelegate
import my.itgungnir.adapter.adapter.VH
import test.itgungnir.adapter.R

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-09
 */
class GridDelegate : BaseDelegate<GridListItem>() {

    override fun layoutId(): Int = R.layout.item_grid

    override fun onRender(holder: VH, data: GridListItem, payloads: MutableList<Bundle?>) {
        holder.itemView.findViewById<TextView>(R.id.item_grid_text).text = data.text
    }
}
