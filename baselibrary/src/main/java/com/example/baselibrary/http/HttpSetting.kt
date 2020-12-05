package com.example.baselibrary.http

import com.example.baselibrary.http.okhttp.OkHttp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * 网络接口配置
 *  目前还没想好如何配置
 */
class HttpSetting {


    fun init(){
//Okhttp对象

       OkHttp.okHttpClient.connectTimeout(30, TimeUnit.SECONDS)


    }


}