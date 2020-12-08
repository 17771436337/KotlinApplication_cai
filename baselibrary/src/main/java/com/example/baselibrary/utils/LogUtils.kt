package com.example.baselibrary.utils

import android.text.TextUtils
import android.util.Log

class LogUtils {

    var className //类名
            : String? = null
    var methodName //方法名
            : String? = null
    var lineNumber //行数
            = 0

    private var TAG :String = "LogUtils";

    /**
     * 获取文件名、方法名、所在行数
     * @param sElements
     */
    private fun getMethodNames(sElements: Array<StackTraceElement>) {
        className = sElements[1].fileName
        methodName = sElements[1].methodName
        lineNumber = sElements[1].lineNumber
    }

    private fun createLog (log: String):String{
        getMethodNames(Throwable().stackTrace)
        val buffer = StringBuffer()
        buffer.append("========$className : $methodName =======\r\n")
        buffer.append(log)
        return buffer.toString()
    }

    /**静态语法块*/
    companion object{

        private val instance : LogUtils by lazy(LazyThreadSafetyMode.NONE) {
            LogUtils()
        }


        open fun v(log: String){
            Log.v(instance.TAG,instance.createLog(log))
        }

        open fun i(log: String){
            Log.i(instance.TAG,instance.createLog(log))
        }

        open fun d(log: String){
            Log.d(instance.TAG,instance.createLog(log))
        }

        open fun e(log: String){
            Log.e(instance.TAG,instance.createLog(log))
        }

        open fun w(log: String){
            Log.w(instance.TAG,instance.createLog(log))
        }

        open fun wtf(log: String){
            Log.wtf(instance.TAG,instance.createLog(log))
        }

        open fun setTAG(tag:String){
            instance.TAG = tag;
        }
    }
}