package com.example.baselibrary.network.retrofit
import com.example.baselibrary.network.other.HttpSetting.Companion.INSTANCE
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    var retrofit: Retrofit? = null
    init {
        if (null != INSTANCE.getBaseUrl()){
      retrofit = Retrofit.Builder()
            .client(INSTANCE.getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .baseUrl(INSTANCE.getBaseUrl())
            .build()
        }else{
            retrofit = Retrofit.Builder()
                .client(INSTANCE.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }

    companion object{

        val instance: RetrofitFactory
            get() = RetrofitFactory()
    }

    /**重新设置基础baseUrl*/
    fun changeBaseUrl(baseUrl: String) {
        retrofit = Retrofit.Builder()
            .client(INSTANCE.getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()

    }




    /**创建*/
    fun<T> create(service :Class<T>): T? {
     return instance.retrofit?.create(service)
    }


    /**
     * 设置订阅 和 所在的线程环境
     */
    fun <T> toSubscribe(o: Observable<T>, s: DisposableObserver<T>) {
        o.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .retry(INSTANCE.RETRY_COUNT.toLong())//请求失败重连次数
            .subscribe(s)
    }

}