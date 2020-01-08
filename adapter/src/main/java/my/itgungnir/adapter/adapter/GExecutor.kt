package my.itgungnir.adapter.adapter

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-08
 */
class GExecutor private constructor() {

    companion object {
        val instance: ExecutorService by lazy {
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        }
    }
}
