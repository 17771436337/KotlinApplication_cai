package com.example.baselibrary.network.retrofit

/**接口返回的数据*/
interface OnSuccessAndFaultListener{

    fun onSuccess(result:String)

    fun onFault(errorMsg: String)

}