package com.example.baselibrary.okhttp.listener

/**进度条回调接口*/
interface BaseProgressListener {
    fun downloadProgress(url:String,total: Long, current: Long)

    fun downloadProgressOnMainThread(url:String,total: Long, current: Long)

    fun uploadProgress(fileName: String, total: Long, current: Long)

    fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long)
}