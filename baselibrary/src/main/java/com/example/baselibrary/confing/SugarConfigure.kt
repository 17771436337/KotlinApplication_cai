package com.example.baselibrary.confing

import android.app.Application
import okhttp3.internal.wait

/**
 * app统一配置模块 需要的模块自行添加即可
 */
abstract class SugarConfigure( application : Application?): AppConfigureDelegate {
    protected var mApplication: Application? = application

    /**设置加载属性*/
    override fun getAppSetting(): AppSetting? {
        return AppSetting.builder()
            .with(getApplication())
            .setCrashErrorListener(getCrashErrorResponse())
            .setResponseErrorListener(getErrorResponse())
            .setHttpSetting(getHttpSetting())
            .build()
    }

    /**返回对应得数据属性*/
    override fun getApplication(): Application? {
        return mApplication
    }



}