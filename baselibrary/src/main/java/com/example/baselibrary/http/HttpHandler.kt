package com.example.baselibrary.http

import com.example.baselibrary.mvp.Constant
import com.example.baselibrary.utils.HandlerUtils

/**Handler接收类*/
class HttpHandler <Model>(httpView : HttpView<Model>?) : HandlerUtils.UiMessageCallback{
    private var httpView : HttpView<Model>? = httpView

    /**注册*/
    fun registered(){
        HandlerUtils.getInstance().addListener(this)
    }


    /**取消注册*/
    fun unRegistered(){
        HandlerUtils.getInstance().removeListener(this)
    }

    override fun handleMessage(localMessage: HandlerUtils.UiMessage?) {

        when (localMessage?.id) {
            Constant.SEND_HTTP_HANDLER_MODE_TYPR -> {
                var msg: HttpHandlerData<Model> = localMessage.obj as HttpHandlerData<Model>
                when(msg.code){
                    HttpType.OK ->{
                        httpView?.onSuccess(msg.url,msg.data.date)
                    }
                    HttpType.ERROR ->{
                        var error: BaseData<Exception> = msg.data as BaseData<Exception>
                        httpView?.onError(msg.url,error.date)
                    }
                }
            }
        }

    }
}