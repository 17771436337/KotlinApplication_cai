package com.example.baselibrary.http.okhttp.callback

import com.example.baselibrary.http.okhttp.listener.BaseProgressListener
import com.example.baselibrary.http.okhttp.DownloadBean
import okhttp3.Call
import okhttp3.Response
/**参数回调*/
abstract class ResultCallBack :
    BaseProgressListener {
    val urlToBeanMap= hashMapOf<String, DownloadBean>()

    abstract fun start()

    abstract fun response(call: Call, response: Response)

    abstract fun failure(call: Call, e: Exception)

    abstract fun responseBodyGetNull(call: Call, response: Response)

    abstract fun otherException(call: Call, response: Response, e: Exception)
}