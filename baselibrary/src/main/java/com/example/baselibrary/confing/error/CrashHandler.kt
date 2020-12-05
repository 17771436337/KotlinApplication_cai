package com.example.baselibrary.confing.error

import android.app.Application
import android.os.Process

/**错误监听*/
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler  {

    private val TAG = CrashHandler::class.java.simpleName

    companion object{
        val instance: CrashHandler by lazy{
            CrashHandler()
        }
    }

    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    private var application: Application? = null
    private var responseErrorListener: CrashErrorListener? = null





    /**
     * 初始化
     */
    fun init(application: Application?) {
        this.application = application
        //记录下默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler?.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
            }
            Process.killProcess(Process.myPid())
            System.exit(10)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        responseErrorListener?.handleResponseError(application, ex)
        return true
    }

    fun responseErrorListener(responseErrorListener: CrashErrorListener?) {
        this.responseErrorListener = responseErrorListener
    }
}