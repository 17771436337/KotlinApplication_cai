package com.example.baselibrary.confing

import android.app.Application
import com.example.baselibrary.confing.error.CrashHandler
import me.jessyan.rxerrorhandler.core.RxErrorHandler

enum class AppConfig {
    INSTANCE;

    private var mAppSetting: AppSetting? = null
     get() {return mAppSetting!!}
    private var mSugarConfigure: SugarConfigure? = null
            get() { return mSugarConfigure}

    open fun getApplication(): Application? {
        return mAppSetting?.getApplication()
    }


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
            .with(mAppSetting?.getApplication())
            .responseErrorListener(mAppSetting?.getResponseErrorListener())
            .build()
    }


    /**
     * 安卓错误日志收集
     */
        fun initCrashHandler(){
            var crashHandler : CrashHandler = CrashHandler.instance
            crashHandler.init(getApplication())
            crashHandler.responseErrorListener(mAppSetting?.getCrashErrorListener())

        }

}