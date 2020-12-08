package com.example.baselibrary.network.okhttp.callback

import com.example.baselibrary.network.model.BaseModel
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class GsonCallBack<E> : DataCallBack<E>() {
    private val gson = Gson()
    override fun stringToData(string: String): E {
        val type=(javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
//       return if (type is ParameterizedType) {
//             parseParameterizedType(string, type)
//        } else if (type is Class<*>) {
//            parseClass(string, type)
//        } else {
//            parseType(string, type)
//        }
        return gson.fromJson(string, type)
    }


    fun parseClass(string: String, rawType:  (Class<*>)):E{

        return if (rawType == String::class.java) {
            string as E
        } else if (rawType == JSONObject::class.java) {
            JSONObject(string) as E
        } else if (rawType == JSONArray::class.java) {
            JSONArray(string) as E
        } else {
            val t =gson.fromJson(string, rawType)
            t as E
        }
    }

    fun parseType(string: String, type: Type):E{
        val t: E =gson.fromJson(string, type)
        return t
    }

    fun  parseParameterizedType(string: String, type: ParameterizedType) : E {
        val rawType = type.rawType // 泛型的实际类型
        val typeArgument = type.actualTypeArguments[0] // 泛型的参数
        return if (rawType !== BaseModel::class.java) {
            gson.fromJson(string, typeArgument)
        }else{
            val model = gson.fromJson(string, BaseModel::class.java)
            model as E
        }
    }
}