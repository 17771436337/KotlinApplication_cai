package com.example.baselibrary.network.other
import com.example.baselibrary.anno.HttpType
import com.example.baselibrary.network.retrofit.OnSuccessAndFaultListener
import com.example.baselibrary.network.retrofit.OnSuccessAndFaultSub
import com.example.baselibrary.network.model.HttpMseeageEvent
import com.example.baselibrary.network.okhttp.OkHttp
import com.example.baselibrary.network.okhttp.callback.DataCallBack
import com.example.baselibrary.network.okhttp.callback.StringCallBack
import com.example.baselibrary.network.retrofit.RetrofitFactory
import io.reactivex.Observable
import okhttp3.Call
import okhttp3.Response
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus

class HttpManager private constructor() {

    companion object{
        val INSTANCE: HttpManager by lazy{
            HttpManager()
        }
    }
    /**
     * 发送给指令的方法集
     */
     fun sendCmd(url:String, params:Map<String,String>?, headers:Map<String,String>?){
        difference(url,params,headers)
    }
    /**
     * 发送给指令的方法集
     */
     fun sendCmd(url:String,params:Map<String,String>?){
        sendCmd(url, params,null)
    }

    /**
     * 发送给指令的方法集
     */
     fun sendCmd(url:String){
        sendCmd(url, null,null)
    }

    /**
     * 使用Retrofit实现的接口请求，在对应的Activity使用注解实现回调《并且使用了EventBus，增强粘性》
     */
    fun sendCmd(url:String,observable: Observable<ResponseBody>?) {
        observable?.let {
            HttpSetting.INSTANCE.getRetrofitFactory().toSubscribe(it, OnSuccessAndFaultSub(object :
                OnSuccessAndFaultListener {
                override fun onSuccess(result: String) {
                    EventBus.getDefault().post(HttpMseeageEvent(url,HttpType.OK,result))
                }
                override fun onFault(errorMsg: String) {
                    EventBus.getDefault().post(HttpMseeageEvent(url,HttpType.ERROR,errorMsg))
                }
            }))
        }

    }

    /**发送消息出去*/
    private fun difference(url: String, params: Map<String, String>?, headers: Map<String, String>?){
        OkHttp.get(url)
                .params(params)
                .addHeader(headers)
                .execute(object :StringCallBack() {
                    override fun getData(rawBodyString: String, call: Call, response: Response) {
                        EventBus.getDefault().post(HttpMseeageEvent(url,HttpType.OK,rawBodyString))
                    }

                    override fun failure(call: Call, e: Exception) {
                        EventBus.getDefault().post(e.message?.let {
                            HttpMseeageEvent(url,HttpType.OK,
                                it)
                        })
                    }


                })

    }



    /**创建Retrofit*/
    fun<T> create(service :Class<T>): T? {
        return RetrofitFactory.instance.retrofit?.create(service)
    }




}