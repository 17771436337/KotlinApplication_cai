package com.example.baselibrary.http

/**接口返回的数据*/
interface HttpView<T> {

    /**接口返回的成功数据*/
   fun onSuccess(url:String ,data:T?);

    /**接口返回失败的数据*/
    fun onError( url:String ,e: Exception?);

}