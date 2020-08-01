package com.example.baselibrary.okhttp.callback

import com.example.baselibrary.okhttp.OkHttp
import okhttp3.Call
import okhttp3.Response

abstract class DataCallBack <E>() : ResultCallBack() {
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
            val data = stringToData(preProcessBodyString(rawString))
            OkHttp.mainHandler.post {
                if (data!=null){
                    getData(data, rawString, call, response)
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

    abstract fun stringToData(string:String):E

    open fun preProcessBodyString(bodyString:String):String{
        return bodyString
    }

    override fun otherException(call: Call, response: Response, e: Exception) {
        failure(call,e)
    }

    override fun downloadProgress(url:String,total: Long, current: Long) {

    }

    override fun downloadProgressOnMainThread(url:String,total: Long, current: Long) {

    }

    override fun uploadProgress(fileName: String, total: Long, current: Long) {
        OkHttp.mainHandler.post {
            uploadProgressOnMainThread(fileName, total, current)
        }
    }

    override fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long) {

    }

    override fun responseBodyGetNull(call: Call, response: Response) {}

    abstract fun getData(data: E, rawBodyString: String, call: Call, response: Response)
}