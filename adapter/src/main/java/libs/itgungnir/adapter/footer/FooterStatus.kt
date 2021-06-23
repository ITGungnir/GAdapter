package libs.itgungnir.adapter.footer

/**
 * 上拉刷新的Footer的状态类
 *
 * Created by ITGungnir on 2020-01-07
 */
data class FooterStatus(val status: Status) {

    /**
     * Footer的状态枚举
     */
    enum class Status(val flag: Int) {
        /**
         * 默认状态
         * 用于列表加载成功、且仍有下一页的情况
         */
        IDLE(0),
        /**
         * 正在加载的状态
         * 用于正在进行网络请求或其他耗时操作的情况
         */
        PROGRESSING(1),
        /**
         * 没有更多的状态
         * 用于列表加载成功、但没有更多数据的情况
         */
        NO_MORE(2),
        /**
         * 加载失败的状态
         * 用于上拉加载失败的情况
         */
        FAILED(3)
    }
}
