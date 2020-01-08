package my.itgungnir.adapter.adapter

import my.itgungnir.adapter.EXECUTOR_THREAD_COUNT
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 一个线程池
 *
 * 由于AsyncListDiffer更新数据时，每次都需要生成一个新的List，
 * 在列表数据量非常巨大的时候，在主线程中复制一个新的List可能会造成UI卡顿，因此考虑使用线程池实现，
 * 在子线程中复制列表，然后post到主线程中刷新RecyclerView的UI
 *
 * Created by ITGungnir on 2020-01-08
 */
class GExecutor private constructor() {

    companion object {
        val instance: ExecutorService by lazy {
            Executors.newFixedThreadPool(EXECUTOR_THREAD_COUNT)
        }
    }
}
