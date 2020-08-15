package com.example.baselibrary.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.IntRange
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap


class ThreadUtils {
    /**静态语法块*/
    companion object {

        private const val NEW = 0
        private const val RUNNING = 1
        private const val EXCEPTIONAL = 2
        private const val COMPLETING = 3
        private const val CANCELLED = 4
        private const val INTERRUPTED = 5
        private const val TIMEOUT = 6
        //---------------------
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()//cpu数
        private val TYPE_PRIORITY_POOLS: HashMap<Byte, HashMap<Int, ExecutorService>?> = hashMapOf()
        private var TASK_POOL_MAP: HashMap<Task<*>, ExecutorService> = hashMapOf()
        private val TIMER = Timer()

        private const val TYPE_SINGLE: Byte = -1
        private const val TYPE_CACHED: Byte = -2
        private const val TYPE_IO: Byte = -4
        private const val TYPE_CPU: Byte = -8
        private val HANDLER: Handler = Handler(Looper.getMainLooper())

        private var sDeliver: Executor? = null

        /**
         * Return whether the thread is the main thread.
         *
         * @return `true`: yes<br></br>`false`: no
         */
        open fun isMainThread(): Boolean {
            return Looper.myLooper() == Looper.getMainLooper()
        }

        open fun getMainHandler(): Handler? {
            return HANDLER
        }

        //-----------------------创建---------------------------
        /**创建主线程*/
        open fun runOnUiThread(runnable: Runnable) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run()
            } else {
                HANDLER.post(runnable)
            }
        }
        /**创建耗时主线程*/
        open fun runOnUiThreadDelayed(runnable: Runnable?, delayMillis: Long) {
            HANDLER.postDelayed(runnable!!, delayMillis)
        }
        //----------------------创建线程池----------------------
        /**
         * 返回创建的线程池
         * @param type 创建类型
         * @param priority 线程个数
         */
        private fun getPoolByTypeAndPriority(type: Byte, priority: Int): ExecutorService {
            synchronized(TYPE_PRIORITY_POOLS) {
                var pool: ExecutorService
                var priorityPools :HashMap<Int, ExecutorService>? =TYPE_PRIORITY_POOLS[type]
                if (priorityPools == null) {
                    pool = ThreadPoolExecutorUtil.createPool(type, priority)
                    priorityPools = hashMapOf(priority to pool)
                    TYPE_PRIORITY_POOLS[type] = priorityPools
                } else {
                    pool = priorityPools.getValue(priority)
                    if (pool == null) {
                        pool = ThreadPoolExecutorUtil.createPool(type, priority)
                        priorityPools[priority] = pool
                    }
                }
                return pool
            }
        }
        /**
         * 返回创建的线程池
         * @param type 创建类型
         */
        private fun getPoolByTypeAndPriority(type: Byte): ExecutorService {
            return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY)
        }
        //---------------------------创建对应的线程-----------------------------------

        /**创建线程*/
        private fun<T> execute(pool:ExecutorService,task:Task<T>, delay:Long,period:Long, unit:TimeUnit){

                synchronized(TASK_POOL_MAP){
                    if (TASK_POOL_MAP[task] != null) {
                        Log.e("ThreadUtils", "Task can only be executed once.")
                        return
                    }
                    TASK_POOL_MAP.put(task, pool)
                }


            if (period.toInt() == 0) {
                if (delay.toInt() == 0) {
                    pool.execute(task)
                } else {
                    val timerTask: TimerTask = object : TimerTask() {
                      override fun run() {
                            pool.execute(task)
                        }
                    }
                    TIMER.schedule(timerTask,unit.toMillis(delay) )
                }
            } else {
                task.setSchedule(true)
                val timerTask: TimerTask = object : TimerTask() {
                    override fun run() {
                        pool.execute(task)
                    }
                }
                TIMER.scheduleAtFixedRate(
                    timerTask,
                    unit.toMillis(delay),
                    unit.toMillis(period)
                )
            }

        }

        private fun <T> executeWithDelay(pool: ExecutorService, task: Task<T>, delay: Long, unit: TimeUnit) {
            execute(pool, task, delay, 0, unit)
        }

        private fun <T> executeAtFixedRate(pool: ExecutorService, task: Task<T>, delay: Long, period: Long, unit: TimeUnit) { execute(pool, task, delay, period, unit) }

        private fun<T> execute(pool:ExecutorService,task:Task<T>){
            execute(pool,task,0,0,TimeUnit.MINUTES)
        }

        //-----------------------------取消对应的线程---------------------------
        /**
         * Cancel the tasks in pool.
         *
         * @param executorService The pool.
         */
        open fun cancel(executorService: ExecutorService) {
            if (executorService is ThreadPoolExecutorUtil) {
                    for ((key, value) in TASK_POOL_MAP.entries) {
                        if (value === executorService) {
                            cancel(key)
                        }
                }

            } else {
                Log.e("ThreadUtils", "The executorService is not ThreadUtils's pool.")
            }
        }

        /**
         * Cancel the given task.
         *
         * @param task The task to cancel.
         */
        open fun cancel(task: Task<*>?) {
            task?.cancel()
        }

        /**
         * Cancel the given tasks.
         *
         * @param tasks The tasks to cancel.
         */
        open fun canel(vararg  task:Task<*>?){
            task?.forEach {
                it?.cancel()
            }
        }

        /**
         * Cancel the given tasks.
         *
         * @param tasks The tasks to cancel.
         */
        open fun cancel(tasks: List<Task<*>?>?) {
            tasks?.forEach{
                it?.cancel()
            }
        }


        //-------------------获取线程池---------------
        /**
         * 返回一个线程池，该线程池重用固定数量的线程，在需要时使用提供的ThreadFactory创建新线程。
         * @param size The size of thread in the pool.
         * @return a fixed thread pool
         */
        open fun getFixedPool(@IntRange(from = 1) size: Int):ExecutorService?{
            return getPoolByTypeAndPriority(size.toByte())
        }

        /**
         *返回一个线程池，该线程池重用固定数量的线程，在需要时使用提供的ThreadFactory创建新线程。
         * @param size     The size of thread in the pool.
         * @param priority The priority of thread in the poll.
         * @return a fixed thread pool
         */
        open fun getFixedPool(@IntRange(from = 1) size: Int, @IntRange(from = 1, to = 10) priority: Int): ExecutorService? {
            return getPoolByTypeAndPriority(size.toByte(), priority)
        }

        /**
         * 返回一个线程池，该线程池重用固定数量的线程，在需要时使用提供的ThreadFactory创建新线程。
         * @return a single thread pool
         */
        open fun getSinglePool(): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_SINGLE)
        }

        /**
         *返回一个线程池，该线程池重用固定数量的线程，在需要时使用提供的ThreadFactory创建新线程。
         * @param priority The priority of thread in the poll.
         * @return a single thread pool
         */
        open fun getSinglePool(@IntRange(from = 1, to = 10) priority: Int): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_SINGLE, priority)
        }

        /**
         *返回一个线程池，该线程池重用固定数量的线程，在需要时使用提供的ThreadFactory创建新线程。
         * @return a cached thread pool
         */
        open fun getCachedPool(): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_CACHED)
        }

        /**
         * Return a thread pool that creates new threads as needed, but
         * will reuse previously constructed threads when they are
         * available.
         *
         * @param priority The priority of thread in the poll.
         * @return a cached thread pool
         */
        open fun getCachedPool(@IntRange(from = 1, to = 10) priority: Int): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_CACHED, priority)
        }

        /**
         * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
         * operating off a queue which size is 128.
         *
         * @return a IO thread pool
         */
        open fun getIoPool(): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_IO)
        }

        /**
         * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
         * operating off a queue which size is 128.
         *
         * @param priority The priority of thread in the poll.
         * @return a IO thread pool
         */
        open fun getIoPool(@IntRange(from = 1, to = 10) priority: Int): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_IO, priority)
        }

        /**
         * Return a thread pool that creates (CPU_COUNT + 1) threads
         * operating off a queue which size is 128 and the maximum
         * number of threads equals (2 * CPU_COUNT + 1).
         *
         * @return a cpu thread pool for
         */
        open fun getCpuPool(): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_CPU)
        }

        /**
         * Return a thread pool that creates (CPU_COUNT + 1) threads
         * operating off a queue which size is 128 and the maximum
         * number of threads equals (2 * CPU_COUNT + 1).
         *
         * @param priority The priority of thread in the poll.
         * @return a cpu thread pool for
         */
        open fun getCpuPool(@IntRange(from = 1, to = 10) priority: Int): ExecutorService? {
            return getPoolByTypeAndPriority(TYPE_CPU, priority)
        }


        //-----------------线程池中执行给定的任务------------------------------

        /**
         * Executes the given task in a fixed thread pool.
         *
         * @param size The size of thread in the fixed thread pool.
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        open fun <T> executeByFixed(
            @IntRange(from = 1) size: Int,
            task: Task<T>
        ) {
            execute(getPoolByTypeAndPriority(size.toByte()), task)
        }

        /**
         * Executes the given task in a fixed thread pool.
         *
         * @param size     The size of thread in the fixed thread pool.
         * @param task     The task to execute.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByFixed(
            @IntRange(from = 1) size: Int,
            task: Task<T>?,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            execute(getPoolByTypeAndPriority(size.toByte(), priority), task!!)
        }

        /**
         * Executes the given task in a fixed thread pool after the given delay.
         *
         * @param size  The size of thread in the fixed thread pool.
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeByFixedWithDelay(
            @IntRange(from = 1) size: Int,
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(getPoolByTypeAndPriority(size.toByte()), task, delay, unit)
        }

        /**
         * Executes the given task in a fixed thread pool after the given delay.
         *
         * @param size     The size of thread in the fixed thread pool.
         * @param task     The task to execute.
         * @param delay    The time from now to delay execution.
         * @param unit     The time unit of the delay parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByFixedWithDelay(
            @IntRange(from = 1) size: Int,
            task: Task<T>,
            delay: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeWithDelay(
                getPoolByTypeAndPriority(size.toByte(), priority),
                task, delay,
                unit
            )
        }

        /**
         * Executes the given task in a fixed thread pool at fix rate.
         *
         * @param size   The size of thread in the fixed thread pool.
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeByFixedAtFixRate(
            @IntRange(from = 1) size: Int,
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(size.toByte()),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a fixed thread pool at fix rate.
         *
         * @param size     The size of thread in the fixed thread pool.
         * @param task     The task to execute.
         * @param period   The period between successive executions.
         * @param unit     The time unit of the period parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByFixedAtFixRate(
            @IntRange(from = 1) size: Int,
            task: Task<T>,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(size.toByte(), priority),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a fixed thread pool at fix rate.
         *
         * @param size         The size of thread in the fixed thread pool.
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByFixedAtFixRate(
            @IntRange(from = 1) size: Int,
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(size.toByte()),
                task, initialDelay, period,
                unit
            )
        }

        /**
         * Executes the given task in a fixed thread pool at fix rate.
         *
         * @param size         The size of thread in the fixed thread pool.
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param priority     The priority of thread in the poll.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByFixedAtFixRate(
            @IntRange(from = 1) size: Int,
            task: Task<T>?,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit?,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(size.toByte(), priority),
                task!!,
                initialDelay,
                period,
                unit!!
            )
        }

        /**
         * Executes the given task in a single thread pool.
         *
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        fun <T> executeBySingle(task: Task<T>) {
            execute(getPoolByTypeAndPriority(TYPE_SINGLE), task)
        }

        /**
         * Executes the given task in a single thread pool.
         *
         * @param task     The task to execute.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeBySingle(
            task: Task<T>,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task)
        }

        /**
         * Executes the given task in a single thread pool after the given delay.
         *
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeBySingleWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE), task, delay, unit)
        }

        /**
         * Executes the given task in a single thread pool after the given delay.
         *
         * @param task     The task to execute.
         * @param delay    The time from now to delay execution.
         * @param unit     The time unit of the delay parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeBySingleWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeWithDelay(
                getPoolByTypeAndPriority(TYPE_SINGLE, priority),
                task, delay,
                unit
            )
        }

        /**
         * Executes the given task in a single thread pool at fix rate.
         *
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeBySingleAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a single thread pool at fix rate.
         *
         * @param task     The task to execute.
         * @param period   The period between successive executions.
         * @param unit     The time unit of the period parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeBySingleAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE, priority),
                task,
                0,
                period,
                unit
            )
        }

        /**
         * Executes the given task in a single thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeBySingleAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE),
                task,
                initialDelay,
                period,
                unit
            )
        }

        /**
         * Executes the given task in a single thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param priority     The priority of thread in the poll.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeBySingleAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE, priority),
                task,
                initialDelay,
                period,
                unit
            )
        }

        /**
         * Executes the given task in a cached thread pool.
         *
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        fun <T> executeByCached(task: Task<T>) {
            execute(getPoolByTypeAndPriority(TYPE_CACHED), task)
        }

        /**
         * Executes the given task in a cached thread pool.
         *
         * @param task     The task to execute.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCached(
            task: Task<T>,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task)
        }

        /**
         * Executes the given task in a cached thread pool after the given delay.
         *
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeByCachedWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED), task, delay, unit)
        }

        /**
         * Executes the given task in a cached thread pool after the given delay.
         *
         * @param task     The task to execute.
         * @param delay    The time from now to delay execution.
         * @param unit     The time unit of the delay parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCachedWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeWithDelay(
                getPoolByTypeAndPriority(TYPE_CACHED, priority),
                task, delay,
                unit
            )
        }

        /**
         * Executes the given task in a cached thread pool at fix rate.
         *
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeByCachedAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a cached thread pool at fix rate.
         *
         * @param task     The task to execute.
         * @param period   The period between successive executions.
         * @param unit     The time unit of the period parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCachedAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED, priority),
                task,
                0,
                period,
                unit
            )
        }

        /**
         * Executes the given task in a cached thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByCachedAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED),
                task,
                initialDelay,
                period,
                unit
            )
        }

        /**
         * Executes the given task in a cached thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param priority     The priority of thread in the poll.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByCachedAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED, priority),
                task,
                initialDelay,
                period,
                unit
            )
        }

        /**
         * Executes the given task in an IO thread pool.
         *
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        fun <T> executeByIo(task: Task<T>) {
            execute(getPoolByTypeAndPriority(TYPE_IO), task)
        }

        /**
         * Executes the given task in an IO thread pool.
         *
         * @param task     The task to execute.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByIo(
            task: Task<T>,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            execute(getPoolByTypeAndPriority(TYPE_IO, priority), task)
        }

        /**
         * Executes the given task in an IO thread pool after the given delay.
         *
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeByIoWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(getPoolByTypeAndPriority(TYPE_IO), task, delay, unit)
        }

        /**
         * Executes the given task in an IO thread pool after the given delay.
         *
         * @param task     The task to execute.
         * @param delay    The time from now to delay execution.
         * @param unit     The time unit of the delay parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByIoWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit)
        }

        /**
         * Executes the given task in an IO thread pool at fix rate.
         *
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeByIoAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, 0, period, unit)
        }

        /**
         * Executes the given task in an IO thread pool at fix rate.
         *
         * @param task     The task to execute.
         * @param period   The period between successive executions.
         * @param unit     The time unit of the period parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByIoAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_IO, priority),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in an IO thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByIoAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_IO),
                task, initialDelay, period,
                unit
            )
        }

        /**
         * Executes the given task in an IO thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param priority     The priority of thread in the poll.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByIoAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_IO, priority), task, initialDelay, period, unit
            )
        }

        /**
         * Executes the given task in a cpu thread pool.
         *
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        fun <T> executeByCpu(task: Task<T>) {
            execute(getPoolByTypeAndPriority(TYPE_CPU), task)
        }

        /**
         * Executes the given task in a cpu thread pool.
         *
         * @param task     The task to execute.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCpu(
            task: Task<T>,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task)
        }

        /**
         * Executes the given task in a cpu thread pool after the given delay.
         *
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeByCpuWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU), task, delay, unit)
        }

        /**
         * Executes the given task in a cpu thread pool after the given delay.
         *
         * @param task     The task to execute.
         * @param delay    The time from now to delay execution.
         * @param unit     The time unit of the delay parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCpuWithDelay(
            task: Task<T>,
            delay: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeWithDelay(
                getPoolByTypeAndPriority(TYPE_CPU, priority),
                task, delay,
                unit
            )
        }

        /**
         * Executes the given task in a cpu thread pool at fix rate.
         *
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeByCpuAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a cpu thread pool at fix rate.
         *
         * @param task     The task to execute.
         * @param period   The period between successive executions.
         * @param unit     The time unit of the period parameter.
         * @param priority The priority of thread in the poll.
         * @param <T>      The type of the task's result.
        </T> */
        fun <T> executeByCpuAtFixRate(
            task: Task<T>,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU, priority),
                task, 0, period,
                unit
            )
        }

        /**
         * Executes the given task in a cpu thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByCpuAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU),
                task, initialDelay, period,
                unit
            )
        }

        /**
         * Executes the given task in a cpu thread pool at fix rate.
         *
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param priority     The priority of thread in the poll.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByCpuAtFixRate(
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit,
            @IntRange(from = 1, to = 10) priority: Int
        ) {
            executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU, priority), task, initialDelay, period, unit
            )
        }

        /**
         * Executes the given task in a custom thread pool.
         *
         * @param pool The custom thread pool.
         * @param task The task to execute.
         * @param <T>  The type of the task's result.
        </T> */
        fun <T> executeByCustom(pool: ExecutorService, task: Task<T>) {
            execute(pool, task)
        }

        /**
         * Executes the given task in a custom thread pool after the given delay.
         *
         * @param pool  The custom thread pool.
         * @param task  The task to execute.
         * @param delay The time from now to delay execution.
         * @param unit  The time unit of the delay parameter.
         * @param <T>   The type of the task's result.
        </T> */
        fun <T> executeByCustomWithDelay(
            pool: ExecutorService,
            task: Task<T>,
            delay: Long,
            unit: TimeUnit
        ) {
            executeWithDelay(pool, task, delay, unit)
        }

        /**
         * Executes the given task in a custom thread pool at fix rate.
         *
         * @param pool   The custom thread pool.
         * @param task   The task to execute.
         * @param period The period between successive executions.
         * @param unit   The time unit of the period parameter.
         * @param <T>    The type of the task's result.
        </T> */
        fun <T> executeByCustomAtFixRate(
            pool: ExecutorService,
            task: Task<T>,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(pool, task, 0, period, unit)
        }

        /**
         * Executes the given task in a custom thread pool at fix rate.
         *
         * @param pool         The custom thread pool.
         * @param task         The task to execute.
         * @param initialDelay The time to delay first execution.
         * @param period       The period between successive executions.
         * @param unit         The time unit of the initialDelay and period parameters.
         * @param <T>          The type of the task's result.
        </T> */
        fun <T> executeByCustomAtFixRate(
            pool: ExecutorService,
            task: Task<T>,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ) {
            executeAtFixedRate(pool, task, initialDelay, period, unit)
        }

        //-----------------------------------
       private  fun getGlobalDeliver(): Executor? {
            if (sDeliver == null) {
                sDeliver = Executor { command -> runOnUiThread(command) }
            }
            return sDeliver
        }
    }



    class ThreadPoolExecutorUtil(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long, unit: TimeUnit?, workQueue: LinkedBlockingQueueUtil, threadFactory: ThreadFactory) :
        ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory) {
        private val  mWorkQueue: LinkedBlockingQueueUtil? = workQueue
        init {
            mWorkQueue?.mPool = this
        }

        companion object{
            fun createPool( type:Byte,priority:Int):ExecutorService{
                when (type){
                    ThreadUtils.TYPE_SINGLE -> {
                        return ThreadPoolExecutorUtil(1,1,1L,TimeUnit.MICROSECONDS,LinkedBlockingQueueUtil(),ThreadFactoryUtile("single",priority))
                    }
                    TYPE_CACHED ->{
                        return ThreadPoolExecutorUtil(1,128,60L,TimeUnit.SECONDS,LinkedBlockingQueueUtil(true),ThreadFactoryUtile("cached",priority))
                    }
                    TYPE_IO ->{
                        return ThreadPoolExecutorUtil(2 * CPU_COUNT + 1,2 * CPU_COUNT + 1,30L,TimeUnit.SECONDS,LinkedBlockingQueueUtil(),ThreadFactoryUtile("IO",priority))
                    }
                    TYPE_CPU ->{
                        return ThreadPoolExecutorUtil(2 * CPU_COUNT + 1,2 * CPU_COUNT + 1,30L,TimeUnit.SECONDS,LinkedBlockingQueueUtil(true),ThreadFactoryUtile("CPU",priority))

                    }
                    else ->{
                        return ThreadPoolExecutorUtil(type.toInt(),type.toInt(),30L,TimeUnit.MICROSECONDS,LinkedBlockingQueueUtil(),ThreadFactoryUtile("fixed(" + type + ")",priority))
                    }

                }
            }
        }


            var mSubmittedCount= AtomicInteger()
             fun getSubmittedCount() : Int{
                return mSubmittedCount.get()
            }

             override fun afterExecute(runnable: Runnable?, t:Throwable?){
                mSubmittedCount.decrementAndGet()
                super.afterExecute(runnable, t)
            }

        override fun execute(command: Runnable?) {
            if (this.isShutdown) return
            mSubmittedCount.incrementAndGet()
            try {
                super.execute(command)
            } catch (ignore: RejectedExecutionException) {
                Log.e("ThreadUtils", "This will not happen!")
                mWorkQueue?.offer(command)
            } catch (t: Throwable) {
                mSubmittedCount.decrementAndGet()
            }
        }

    }


    class LinkedBlockingQueueUtil constructor (): LinkedBlockingQueue<Runnable>() {
      var mPool:ThreadPoolExecutorUtil?= null
      private var mCapacity = Int.MAX_VALUE

        constructor( isAddSubThreadFirstThenAddQueue:Boolean) : this(){
            if (isAddSubThreadFirstThenAddQueue){
                mCapacity = 0;
            }
        }

        constructor(capacity:Int):this(){
            this.mCapacity = capacity
        }

        override fun offer(runnable: Runnable?): Boolean {
            if (mCapacity <= size &&
                mPool != null && mPool?.getPoolSize()!! < mPool?.getMaximumPoolSize()!!
            ) {
                // create a non-core thread
                return false
            }
            return super.offer(runnable)
        }
    }


   protected class  ThreadFactoryUtile constructor(prefix: String, priority: Int)  : AtomicLong(),ThreadFactory{
        private val POOL_NUMBER: AtomicInteger = AtomicInteger(1)

        private var namePrefix: String? = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-"
        private var priority = priority
        private var isDaemon = false



        constructor(prefix: String, priority: Int, isDaemon: Boolean) :this(prefix, priority){ this.isDaemon = isDaemon }

        override fun newThread(p0: Runnable): Thread {
            val t: Thread = object : Thread(p0, namePrefix + andIncrement) {
                override fun run() {
                    try {
                        super.run()
                    } catch (t: Throwable) {
                        Log.e("ThreadUtils", "Request threw uncaught throwable", t)
                    }
                }
            }
            t.isDaemon = isDaemon
            t.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e -> println(e) }
            t.priority = priority
            return t
        }

        override fun toByte(): Byte {
          return get().toByte()
        }

        override fun toChar(): Char {

         return get().toChar()
        }

        override fun toShort(): Short {
           return get().toShort()
        }

    }


     abstract class Task<T> :Runnable{

        private val state = AtomicInteger(NEW)

        //----------------------------
        private var mTimeoutMillis: Long = 0
        private var mTimeoutListener: OnTimeoutListener? = null
        private var runner: Thread? = null
        private var deliver: Executor?= null
        private var isSchedule: Boolean =false

        private var mTimer: Timer? = null

         open fun setSchedule(isSchedule: Boolean) {
            this.isSchedule = isSchedule
        }

        override fun run() {
            if (isSchedule ) {
                if (runner == null) {
                    if (!state.compareAndSet(NEW, RUNNING)) return
                    runner = Thread.currentThread()
                    if (mTimeoutListener != null) {
                        Log.w("ThreadUtils", "Scheduled task doesn't support timeout.")
                    }
                } else {
                    if (state.get() != RUNNING) return
                }
            } else {
                if (!state.compareAndSet(NEW, RUNNING)) return
                runner = Thread.currentThread()
                if (mTimeoutListener != null) {
                    mTimer = Timer()
                    mTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            if (!isDone() && mTimeoutListener != null) {
                                timeout()
                                mTimeoutListener?.onTimeout()
                            }
                        }
                    }, mTimeoutMillis)
                }
            }
            try {
                val result = doInBackground()
                if (isSchedule) {
                    if (state.get() != RUNNING) return
                    getDeliver()?.let {
                        it.execute { onSuccess(result) }
                    }
                } else {
                    if (!state.compareAndSet(RUNNING, COMPLETING)) return
                    getDeliver()?.let {
                        it.execute {
                            onSuccess(result)
                            onDone()
                        }
                    }
                }
            } catch (ignore: InterruptedException) {
                state.compareAndSet(CANCELLED, INTERRUPTED)
            } catch (throwable: Throwable) {
                if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) return
                getDeliver()?.let {
                    it.execute {
                    onFail(throwable)
                    onDone()
                }
                }
            }
        }

         fun cancel(){
            cancel(true)
         }

        private fun cancel(mayInterruptIfRunning:Boolean ){
            synchronized(state){
               if (state.get() > RUNNING) {
                   return
               }
                state.set(COMPLETING)
            }
            if (mayInterruptIfRunning){
                runner?.interrupt()
            }
            getDeliver()?.let {
                it.execute {
                    onCancel()
                    onDone()
                }
            }

        }

        open fun setDeliver(deliver: Executor): Task<T> {
            this.deliver = deliver
            return this
        }

        /**
         * Scheduled task doesn't support timeout.
         */
        open fun setTimeout(
            timeoutMillis: Long,
            listener: OnTimeoutListener): Task<T> {
            mTimeoutMillis = timeoutMillis
            mTimeoutListener = listener
            return this
        }
         open fun timeout() {
            synchronized(state) {
                if (state.get() > RUNNING) return
                state.set(TIMEOUT)
            }
            runner?.interrupt()
            onDone()
        }

        open fun isCanceled(): Boolean {
            return state.get() >= CANCELLED
        }

        open fun isDone(): Boolean {
            return state.get() > RUNNING
        }


        @CallSuper
        protected open fun onDone() {
                TASK_POOL_MAP.remove(this)

            mTimer?.let {
                mTimer?.cancel()
                mTimer = null
                mTimeoutListener = null
            }
        }

         open fun getDeliver(): Executor? {
             if (deliver == null){
                return getGlobalDeliver()
             }else{
                 return deliver
             }

        }
        //----------------抽象方法-----------------
        /**程序执行前《处理运算逻辑》*/
        abstract fun doInBackground(): T

        /**运算逻辑处理完成以后*/
        abstract fun onSuccess(result: T)

        /**关闭线程后*/
        abstract fun onCancel()

        /**线程出错*/
        abstract fun onFail(t: Throwable?)


        //-----------------------------

        interface OnTimeoutListener {
            fun onTimeout()
        }
    }



}