package com.example.myapplication.mvp.main
import com.example.baselibrary.mvp.presenter.BasePresenter
import com.example.baselibrary.okhttp.OkHttp
import com.example.baselibrary.okhttp.callback.GsonCallBack
import okhttp3.Call
import okhttp3.Response

class MainPresenter : BasePresenter<MainView>(){

    fun getData(){

        OkHttp.get("http://ehrtest.chngsl.com:8060/index.php?g=mobile&m=vote&a=options_list")
                .params("vote_id","1496")
            .execute(object : GsonCallBack<Any>(){
                override fun getData(
                    data: Any,
                    rawBodyString: String,
                    call: Call,
                    response: Response
                ) {

                    mView.onMainSuccess(data.toString())
                }

                override fun failure(call: Call, e: Exception) {

                }

            })
    }
}