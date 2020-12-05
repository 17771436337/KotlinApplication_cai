package com.example.baselibrary.confing

import com.example.baselibrary.confing.error.CrashErrorListener
import com.example.baselibrary.http.HttpSetting
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener

interface AppConfigureDelegate {

    /**返回错误日志*/
    fun getCrashErrorResponse(): CrashErrorListener?


    /**
     * 统一异常获取
     */
    fun getErrorResponse(): ResponseErrorListener?



    /**
     * app配置
     */
    fun getAppSetting(): AppSetting?


    fun getHttpSetting(): HttpSetting?
}