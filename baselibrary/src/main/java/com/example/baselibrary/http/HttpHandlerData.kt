package com.example.baselibrary.http
data class HttpHandlerData<T>(var url:String,var code:HttpType,var data:BaseData<T>) {}