package com.example.baselibrary.confing

import android.app.Application
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener

interface AppConfigureDelegate {

    /**返回错误日志*/
    fun getCrashErrorResponse(): CrashErrorListener?


    /**
     * 统一异常获取
     */
    fun getErrorResponse(): ResponseErrorListener?


    /**
     * 获取全局的application
     * @return
     */
    fun getApplication(): Application?


    /**
     * app配置
     */
    fun getAppSetting(): AppSetting?


    fun getHttpSetting():HttpSetting?
}