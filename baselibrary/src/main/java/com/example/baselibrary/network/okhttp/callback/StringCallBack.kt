package com.example.baselibrary.network.okhttp.callback

import com.example.baselibrary.network.okhttp.OkHttp
import okhttp3.Call
import okhttp3.Response

abstract class StringCallBack : ResultCallBack() {

    override fun uploadProgress(fileName: String, total: Long, current: Long) {
        OkHttp.mainHandler.post {
            uploadProgressOnMainThread(fileName, total, current)
        }
    }

    override fun start() {

    }

    override fun response(call: Call, response: Response) {
        val responseBody = response.body
        if (responseBody == null) {
            responseBodyGetNull(call, response)
            return
        }
        try {
            val rawString = responseBody.string()

            OkHttp.mainHandler.post {
                if (rawString?.length > 0){
                    getData( rawString, call, response)
                }else{
                    otherException(call,response,IllegalArgumentException("stringToData() function get null"))
                }

            }
        }catch (e:Exception){
            OkHttp.mainHandler.post {
                otherException(call, response, e)
            }
        }

    }

    override fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long) {}

    override fun responseBodyGetNull(call: Call, response: Response) {}

    abstract fun getData( rawBodyString: String, call: Call, response: Response)

    override fun otherException(call: Call, response: Response, e: Exception) {
        failure(call,e)
    }

    override fun downloadProgress(url:String,total: Long, current: Long) {

    }

    override fun downloadProgressOnMainThread(url:String,total: Long, current: Long) {

    }
}