package com.example.baselibrary.network.other

import com.example.baselibrary.network.okhttp.OkHttp
import com.example.baselibrary.network.okhttp.OkHttp.okHttpClient
import com.example.baselibrary.network.retrofit.RetrofitFactory
import okhttp3.OkHttpClient

/**
 * 网络接口配置
 *  目前还没想好如何配置
 */
class HttpSetting {
    companion object{
       var INSTANCE = HttpSetting()
    }

    /**
     * 请求失败重连次数
     */
     val RETRY_COUNT = 3


     fun getOkHttpClient(): OkHttpClient {
       return okHttpClient
    }

     fun getBaseUrl():String{
        return "https://www.baidu.com/"
    }


    fun getRetrofitFactory():RetrofitFactory{
        return RetrofitFactory.instance
    }





}