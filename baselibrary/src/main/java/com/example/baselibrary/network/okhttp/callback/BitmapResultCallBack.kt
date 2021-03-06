package com.example.baselibrary.network.okhttp.callback

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.baselibrary.network.okhttp.OkHttp
import okhttp3.Call
import okhttp3.Response

abstract class BitmapResultCallBack : ResultCallBack() {
    override fun start() {

    }

    override fun response(call: Call, response: Response) {
        val responseBody = response.body
        if (responseBody==null){
            responseBodyGetNull(call, response)
            return
        }
        try {
            val inputStream = responseBody.byteStream()
            val bitmap= BitmapFactory.decodeStream(inputStream)
            OkHttp.mainHandler.post {
                if (bitmap==null){
                    otherException(call,response,IllegalArgumentException("BitmapFactory.decodeStream() function get null"))
                }else{
                    finish(bitmap)
                }
            }
        }catch (e:Exception){
            OkHttp.mainHandler.post {
                otherException(call, response, e)
            }
        }


    }

    override fun otherException(call: Call, response: Response, e: Exception) {
        failure(call,e)
    }

    override fun responseBodyGetNull(call: Call, response: Response) {}

    abstract fun finish(bitmap: Bitmap)

    override fun downloadProgress(url:String,total: Long, current: Long) {
        OkHttp.mainHandler.post {
            downloadProgressOnMainThread(url,total, current)
        }
    }

    override fun uploadProgress(fileName: String, total: Long, current: Long) {}

    override fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long) {

    }
}