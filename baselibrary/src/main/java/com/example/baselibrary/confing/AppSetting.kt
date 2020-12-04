package com.example.baselibrary.confing

import android.app.Application
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener

class AppSetting(builder: Builder?){
    private val responseErrorListener: ResponseErrorListener? = builder?.responseErrorListener
    private val crashErrorResponse: CrashErrorListener? = builder?.crashErrorResponse
    private val application: Application? = builder?.application
    private val httpSetting:HttpSetting?=builder?.httpSetting

    /**返回全局变量*/
    fun getApplication(): Application? {
        return application
    }

    /**返回错误日志*/
    fun getResponseErrorListener(): ResponseErrorListener? {
        return responseErrorListener
    }

    /**返回缓存错误*/
    fun getCrashErrorListener(): CrashErrorListener? {
        return crashErrorResponse
    }

    /**设置网络参数*/
    fun getHttpSetting():HttpSetting?{
        return httpSetting
    }




    /**获取一个静态数据*/
    companion object Builder{

        fun builder(): Builder {
            return builder()
        }

        var application: Application? = null
        var responseErrorListener: ResponseErrorListener? = null
        var crashErrorResponse: CrashErrorListener? = null
        var httpSetting:HttpSetting? = null


        /**设置全局变量*/
        fun with(application: Application?)= apply {
            this.application = application
        }


        /**
         * 统一异常处理
         * @param responseErrorListener
         * @return
         */
        fun setResponseErrorListener(responseErrorListener: ResponseErrorListener?)= apply{
            this.responseErrorListener = responseErrorListener
        }

        /**缓存错误标记*/
        fun setCrashErrorListener(crashErrorResponse: CrashErrorListener?)= apply {
            this.crashErrorResponse = crashErrorResponse
        }


        fun setHttpSetting(httpSetting: HttpSetting?) = apply{
            this.httpSetting = httpSetting
        }

        /***/
        fun build(): AppSetting {
            checkNotNull(application) { "application is required" }
            checkNotNull(responseErrorListener) { "responseErrorListener is required" }
            return AppSetting(this)
        }

    }
}