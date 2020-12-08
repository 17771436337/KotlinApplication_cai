package com.example.baselibrary.network.model

import com.example.baselibrary.anno.HttpType

/**接口接收数据模型*/
data class BaseModel<T>(var code:Int, var msg:String, var date:T)

/**Handler发送消息的数据模型*/
data class HttpMseeageEvent(var url:String, var code: HttpType, var data: String)

/**下载数据模型*/
data class  DownloadBean( var url:String, var filename:String = "", var filePath:String = "", var downloadLength:Long = 0, var contentLength:Long = 0)