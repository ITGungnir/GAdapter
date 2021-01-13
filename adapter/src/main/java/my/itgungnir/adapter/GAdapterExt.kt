package my.itgungnir.adapter

import my.itgungnir.adapter.adapter.GAdapter
import my.itgungnir.adapter.adapter.ListItem
import my.itgungnir.adapter.footer.FooterDelegate
import my.itgungnir.adapter.footer.FooterStatus
import my.itgungnir.adapter.footer.FooterVO

/**
 * 为GAdapter添加一个自定义的FooterDelegate
 * 这样设计的原因是，用户可以自定义FooterDelegate的样式，更加灵活
 */
fun GAdapter.addFooterDelegate(delegate: FooterDelegate) = addDelegate({ it is FooterVO }, delegate)

/**
 * 如果用户设置了RecyclerView支持上拉加载，则不能调用GAdapter的refresh()方法，而要调用本方法
 * GAdapter的refresh()方法仅在不支持上拉加载的RecyclerView中使用
 *
 * @param list 最新的数据列表，此列表需要是完整的数据列表，而不仅仅是最新一页的数据列表
 * @param hasMore 是否还有下一页
 */
fun GAdapter.refreshWithFooter(list: MutableList<out ListItem>, hasMore: Boolean) {
    // 通过是否还有下一页来更新Footer的展示状态
    if (hasMore) {
        setFooterStatus(FooterStatus.Status.IDLE)
    } else {
        setFooterStatus(FooterStatus.Status.NO_MORE)
    }
    refresh(dataList = list, justRefreshFooter = false)
}

/**
 * 当上拉加载出现错误时，可以调用这个方法将Footer的状态置为FAILED状态
 */
fun GAdapter.loadMoreError() {
    setFooterStatus(FooterStatus.Status.FAILED)
    refresh(justRefreshFooter = true)
}

/**
 * 某些情况下，用户需要实现"当上拉加载失败时，可以通过点击来重试"的业务，此时可以先调用这个方法，
 * 将Footer的状态置为正在加载，然后再重新加载下一页数据
 */
fun GAdapter.loadMoreRetry(retry: () -> Unit) {
    setFooterStatus(FooterStatus.Status.PROGRESSING)
    refresh(justRefreshFooter = true)
    retry.invoke()
}
