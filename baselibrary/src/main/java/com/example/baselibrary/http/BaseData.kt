package com.example.baselibrary.http

data class BaseData<T>(var code:Int, var msg:String, var date:T) {}