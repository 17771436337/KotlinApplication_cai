package com.example.baselibrary
import android.app.Application
import android.util.Log
import com.example.baselibrary.confing.AppConfig
import com.example.baselibrary.confing.SugarConfigure

abstract class LibApplication<S : SugarConfigure>(configure: S) : Application() {

    private var TAG : String? = this.javaClass.simpleName;

    protected var mConfigure: S = configure

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
////        MultiDex.install(this)
//    }

    override fun onCreate() {
        super.onCreate()
        initConfigure()
        AppConfig.INSTANCE.initConfig(mConfigure)
        init()
    }

    /**
     * 初始化app相关配置
     */
    protected abstract fun initConfigure()

    /**
     * 初始化一些操作
     */
    protected abstract fun init()


    /**
     * 程序终止的时候执行
     */
    override fun onTerminate() {
        Log.i(TAG,"onTerminate")
        super.onTerminate()
        // ARouterUtils.destroy();
    }

    /**
     * 低内存的时候执行
     */
    override fun onLowMemory() {
        Log.i(TAG,"onLowMemory")
        super.onLowMemory()
    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    override fun onTrimMemory(level: Int) {
        Log.i(TAG,"onTrimMemory")
        super.onTrimMemory(level)
    }

}