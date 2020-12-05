package com.example.baselibrary.confing

import android.app.Application
import com.example.baselibrary.http.HttpSetting
import okhttp3.internal.wait

/**
 * app统一配置模块 需要的模块自行添加即可
 */
abstract class SugarConfigure( application : Application): AppConfigureDelegate {
    private var mApplication: Application = application
    get() { return field }

    /**设置加载属性*/
    override fun getAppSetting(): AppSetting? {
        return AppSetting.builder()
            .with(mApplication)
            .setCrashErrorListener(getCrashErrorResponse())
            .setResponseErrorListener(getErrorResponse())
            .setHttpSetting(getHttpSetting())
            .build()
    }


    /**设置对应的属性*/
    override fun getHttpSetting(): HttpSetting? {
        return HttpSetting()
    }





}