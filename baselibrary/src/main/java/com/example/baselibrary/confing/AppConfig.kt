package com.example.baselibrary.confing

import android.app.Application
import me.jessyan.rxerrorhandler.core.RxErrorHandler

enum class AppConfig {
    INSTANCE;

    private var mAppSetting: AppSetting? = null
    private var mSugarConfigure: SugarConfigure? = null


    fun initConfig(sugarConfigure: SugarConfigure) {
        this.mSugarConfigure = sugarConfigure;
        this.mAppSetting = sugarConfigure.getAppSetting()

        initCrashHandler();
        getRxErrorHandler();


    }


    /**
     * 统一异常获取处理
     * @return
     */
    open fun getRxErrorHandler(): RxErrorHandler? {
        return RxErrorHandler
            .builder()
            .with(getAppSetting().getApplication())
            .responseErrorListener(getAppSetting().getResponseErrorListener())
            .build()
    }


    /**
     * 安卓错误日志收集
     */
        fun initCrashHandler(){
            var crashHandler : CrashHandler = CrashHandler.instance
            crashHandler.init(getApplication())
            crashHandler.responseErrorListener(getAppSetting().getCrashErrorListener())

        }


    open fun getApplication(): Application? {
        return getAppSetting().getApplication()
    }

    open fun getAppSetting(): AppSetting {
        return mAppSetting!!
    }

    /**
     * 统一的配置
     * @return
     */
    open fun getSugarConfigure(): SugarConfigure? {
        return mSugarConfigure
    }

}