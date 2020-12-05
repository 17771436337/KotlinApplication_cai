package com.example.baselibrary.http
import com.example.baselibrary.http.model.BaseModel
import com.example.baselibrary.http.model.HttpHandlerData
import com.example.baselibrary.http.okhttp.OkHttp
import com.example.baselibrary.http.okhttp.callback.GsonCallBack
import com.example.baselibrary.mvp.Constant
import com.example.baselibrary.utils.HandlerUtils
import okhttp3.Call
import okhttp3.Response

class HttpManager private constructor(){
    companion object{
        val INSTANCE: HttpManager by lazy{
            HttpManager()
        }
    }
    /**
     * 发送给指令的方法集
     */
    open fun<T> sendCmd(url:String,params:Map<String,String>?,headers:Map<String,String>?){
        difference<T>(url,params,headers)
    }
    /**
     * 发送给指令的方法集
     */
    open fun<T> sendCmd(url:String,params:Map<String,String>?){
        sendCmd<T>(url, params,null)
    }

    /**
     * 发送给指令的方法集
     */
    open fun<T> sendCmd(url:String){
        sendCmd<T>(url,null)
    }

    /**发送消息出去*/
    private fun<T> difference(url: String, params: Map<String, String>?, headers: Map<String, String>?){
        OkHttp.get(url)
                .params(params)
                .addHeader(headers)
                .execute(object : GsonCallBack<BaseModel<T>>() {
                    override fun getData(
                            data: BaseModel<T>,
                            rawBodyString: String,
                            call: Call,
                            response: Response
                    ) {
                        /**通过回收消息，进行接口处理*/
                       val msg : HttpHandlerData<T> = HttpHandlerData(url,HttpType.OK,data)
                        HandlerUtils.getInstance().send(Constant.SEND_HTTP_HANDLER_MODE_TYPR,msg);
                    }
                    override fun failure(call: Call, e: Exception) {
                        val msg :HttpHandlerData<Exception> = HttpHandlerData(url,HttpType.ERROR, BaseModel<Exception>(-1,"错误日志",e))
                        HandlerUtils.getInstance().send(Constant.SEND_HTTP_HANDLER_MODE_TYPR,msg)
                    }

                })

    }

}