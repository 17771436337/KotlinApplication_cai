package com.example.baselibrary.confing

import android.content.Context

/**数据库异常捕获*/
interface CrashErrorListener {
    /**发送错误消息*/
    fun handleResponseError(context: Context?, t: Throwable?)
}