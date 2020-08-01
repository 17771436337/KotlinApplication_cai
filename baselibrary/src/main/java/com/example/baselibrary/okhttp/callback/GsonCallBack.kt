package com.example.baselibrary.okhttp.callback

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType

abstract class GsonCallBack<E> : DataCallBack<E>() {
    private val gson = Gson()
    override fun stringToData(string: String): E {
        val type=(javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return gson.fromJson(string,type)
    }
}