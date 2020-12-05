package com.example.baselibrary.http.model

import com.example.baselibrary.http.HttpType

/**接口接收数据模型*/
data class BaseModel<T>(var code:Int, var msg:String, var date:T) {}

/**Handler发送消息的数据模型*/
data class HttpHandlerData<T>(var url:String, var code: HttpType, var data: BaseModel<T>) {}

/**下载数据模型*/
data class  DownloadBean( var url:String, var filename:String = "", var filePath:String = "", var downloadLength:Long = 0, var contentLength:Long = 0){}