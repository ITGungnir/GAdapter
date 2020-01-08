package my.itgungnir.adapter

/**
 * 当Footer的状态发生变化时，会以这个字符串为key，将新的状态以payload的形式提供给适配器
 */
const val PL_FOOTER_STATUS = "PL_FOOTER_STATUS"

/**
 * 线程池中的线程数量
 */
val EXECUTOR_THREAD_COUNT = Runtime.getRuntime().availableProcessors()
