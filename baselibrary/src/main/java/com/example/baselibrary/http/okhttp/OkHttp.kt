package com.example.baselibrary.http.okhttp

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.example.baselibrary.http.okhttp.callback.GlideCallBack
import com.example.baselibrary.http.okhttp.request.BaseRequest
import com.example.baselibrary.utils.NetworkUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object OkHttp {
    var okHttpClient = OkHttpClient()

    val mainHandler = Handler(Looper.getMainLooper())

    val globalHeaderMap = hashMapOf<String, String>()

    val globalParamsMap = hashMapOf<String, String>()

    var preventContinuousRequests=true

    var networkUnavailableForceCache=true

    val statusUrlMap= ConcurrentHashMap<String,Boolean>()

    const val DEFAULT_MEDIA_TYPE_STRING = "application/octet-stream"

    val cachedThreadPool: ExecutorService = Executors.newCachedThreadPool()

    var networkAvailable=true

    var application: Application?=null
        set(value) {
            field=value
            if (value!=null){
                NetworkUtil.init(value)
            }
        }


    fun addGlobalHeader(key: String, value: String) {
        globalHeaderMap[key] = value
    }

    /**
     * append to url
     */
    fun addGlobalParams(key: String, value: String) {
        globalParamsMap[key] = value
    }

    fun  get(url: String): BaseRequest {
        return BaseRequest(url, "get")
    }

    fun  postJson(url: String, jsonObject: JSONObject): BaseRequest {
        val request =
            BaseRequest(url, "postJson")
        request.postJson(jsonObject)
        return request
    }

    fun  post(url: String, valueMap: Map<String, String> = HashMap()): BaseRequest {
        val request =
            BaseRequest(url, "post")
        request.post(valueMap)
        return request
    }

    fun downloadFile(url: String,filename: String,filePath: String): BaseRequest {
        val request= BaseRequest(
            url,
            "downloadFile"
        )
        request.fileName=filename
        request.filePath=filePath
        return request
    }

    fun getBitmap(url: String): BaseRequest {
        return BaseRequest(
            url,
            "getBitmap"
        )
    }

    fun  uploadFile(url: String, file: File, mediaType: String="application/octet-stream"): BaseRequest {
        val request=
            BaseRequest(url, "uploadFile")
        request.uploadFile(file)
        request.defaultFileMediaType=mediaType.toMediaType()
        return request
    }

    fun  postForm(url: String): BaseRequest {
        return BaseRequest(
            url,
            "postForm"
        )
    }

//    fun addInterceptor(interceptor : Interceptor){
//
//    }

    fun <G: GlideCallBack> getGlideClient(listener:G):OkHttpClient{
        return  getBitmap("").prepare(listener)
    }


    fun cancelCall(tag:String){
        val queuedCall=okHttpClient.dispatcher.queuedCalls().firstOrNull{
            it.request().tag().toString()==tag
        }
        queuedCall?.cancel()

        val runningCall=okHttpClient.dispatcher.runningCalls().firstOrNull {
            it.request().tag().toString()==tag
        }
        runningCall?.cancel()
        statusUrlMap.remove(tag)
    }

    fun cancelAll(){
        okHttpClient.dispatcher.cancelAll()
        statusUrlMap.clear()
    }

}