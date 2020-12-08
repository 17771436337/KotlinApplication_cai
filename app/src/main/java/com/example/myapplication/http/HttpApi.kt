package com.example.myapplication.http
import com.example.baselibrary.network.other.HttpManager
import com.example.baselibrary.network.other.HttpSetting
import com.example.myapplication.DataBase
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path
import retrofit2.http.Query

const val LIST : String = "http://ehrtest.chngsl.com:8060/index.php?g=mobile&m=vote&a=options_list"

const val XINWEN:String = "http://v.juhe.cn/toutiao/index"

interface Api {
   @GET(XINWEN)
    fun getXinWen(@Query("type") type: String, @Query("key") key:String): Observable<ResponseBody>

    @HTTP(method = "GET",path = LIST,hasBody = false)
    fun getList(@Query("vote_id") vote_id: String): Observable<ResponseBody>


}

object HttpApi {
     fun sendCmdList(){
//         val observable =    HttpManager.INSTANCE.create(Api::class.java)?.getList("1496")
//         HttpManager.INSTANCE.sendCmd(LIST,observable)
         var params: Map<String, String> = HashMap<String, String>().also {
             it.put("vote_id", "1496")
         }
         HttpManager.INSTANCE.sendCmd(LIST,params)
    }

    fun setXinWen(){
        val observable = HttpManager.INSTANCE.create(Api::class.java)?.getXinWen("junshi","2cf5722b519719df3dea59880c300489")
        HttpManager.INSTANCE.sendCmd(XINWEN,observable)
    }

}


