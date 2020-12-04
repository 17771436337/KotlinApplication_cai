package com.example.baselibrary.http

interface ServerApi {



    fun difference(url:String,params:Map<String,Any>?,headers:Map<String,Any>?);
}