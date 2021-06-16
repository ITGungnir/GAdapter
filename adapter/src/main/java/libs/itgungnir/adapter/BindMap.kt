package libs.itgungnir.adapter

/**
 * BindMap实体类
 *
 * 这个实体类用于标记RecyclableItem和Delegate的一一对应关系
 *
 * Created by ITGungnir on 2019-10-05
 */
data class BindMap(
    val type: Int,
    val isItemForType: (data: RecyclableItem) -> Boolean,
    val delegate: Delegate
)
