package com.example.myapplication.http

import com.example.baselibrary.http.HttpManager
import com.example.myapplication.DataBaseBean

object HttpApi {
    public var LIST : String = "http://ehrtest.chngsl.com:8060/index.php?g=mobile&m=vote&a=options_list";


    public fun sendCmdList(){
        var params: Map<String, String> = HashMap<String, String>().also {
            it.put("vote_id", "1496")
        }
        HttpManager.INSTANCE.sendCmd<DataBaseBean>(LIST,params)
    }

}