package com.example.baselibrary.network.okhttp.request

import com.example.baselibrary.network.model.DownloadBean
import com.example.baselibrary.network.okhttp.OkHttp
import com.example.baselibrary.network.okhttp.body.ProgressRequestBody
import com.example.baselibrary.network.okhttp.body.ProgressResponseBody
import com.example.baselibrary.network.okhttp.callback.ResultCallBack
import com.example.baselibrary.utils.LogUtils
import com.example.baselibrary.utils.NetworkUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.headersContentLength
import org.json.JSONObject
import java.io.File
import java.io.IOException

class BaseRequest(url: String, private val type: String) {

    private var client: OkHttpClient =
        OkHttp.okHttpClient

    var requestCacheControl: CacheControl? = null

    private val paramsMap = hashMapOf<String, String>()

    private val postParamsMap = hashMapOf<String, String>()

    private val formParamsMap = hashMapOf<String, String>()

    /**
     * use the list because the key can be same
     */
    private val formFileKeyList = mutableListOf<String>()

    private val formFileList = mutableListOf<File>()

    private val formFileMediaTypeList = mutableListOf<MediaType>()

    private var requestUrl = url

    var tag = requestUrl

    private val requestBuilder = Request.Builder()

    private var postJSONObject = JSONObject()

    private lateinit var uploadFile: File

    internal var defaultFileMediaType = OkHttp.DEFAULT_MEDIA_TYPE_STRING.toMediaType()

    internal var fileName = ""

    internal var filePath = ""


    fun execute(callBack: ResultCallBack) {
        if (type == "downloadFile") {
            OkHttp.cachedThreadPool.execute {
                prepare(callBack)
                OkHttp.mainHandler.post {
                    process(callBack)
                }
            }
        } else {
            prepare(callBack)
            process(callBack)

        }
    }

    private fun  process(callBack: ResultCallBack) {
        val status = OkHttp.statusUrlMap[tag] ?: false
        if (OkHttp.preventContinuousRequests && status) {
            return
        }
        callBack.start()
        OkHttp.statusUrlMap[tag] = true
        val cache = requestCacheControl
        requestBuilder.url(requestUrl).tag(tag)
        if (OkHttp.networkUnavailableForceCache &&!NetworkUtil.isNetworkAvailable()){
            requestBuilder.cacheControl(CacheControl.FORCE_CACHE)
        }else if (cache != null){
            requestBuilder.cacheControl(cache)
        }
        client.newCall(requestBuilder.build()).enqueue((object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                OkHttp.mainHandler.post {
                    callBack.failure(call, e)
                }
                OkHttp.statusUrlMap.remove(tag)
            }

            override fun onResponse(call: Call, response: Response) {
                callBack.response(call, response)
                OkHttp.statusUrlMap.remove(tag)
            }

        }))
    }

    fun setTheTag(tag: String) = apply {
        this.tag = tag
    }

    fun setTheRequestCacheControl(requestCacheControl: CacheControl) = apply {
        this.requestCacheControl = requestCacheControl
    }


    private fun appendParamsMapToUrl(map: Map<String, String>) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(requestUrl)
        var index = 0
        val haveValueInUrl = requestUrl.indexOf("?") > 0 || requestUrl.indexOf("=") > 0
        for ((k, v) in map) {
            if (index == 0 && !haveValueInUrl) {
                stringBuilder.append("?")
            } else {
                stringBuilder.append("&")
            }
            stringBuilder.append(k)
            stringBuilder.append("=")
            stringBuilder.append(v)
            index++
        }
        requestUrl = stringBuilder.toString()

        LogUtils.v("url:"+requestUrl)
    }


    fun addHeader(key: String, value: String) = apply {
        requestBuilder.header(key, value)
    }

    fun addHeader(headers: Map<String, String>?) = apply {

        headers?.forEach {
            requestBuilder.header(it.key, it.value)
        }

    }

    /**
     * always append to url
     */
    fun params(key: String, value: String) = apply {
        paramsMap[key] = value
    }


    fun  params( params: Map<String, String>?)= apply{
        params?.forEach {
            paramsMap[it.key] = it.value
        }
    }

    /**
     * only in post body
     */
    fun post(key: String, value: String) = apply {
        postParamsMap[key] = value
    }

    fun post(map: Map<String, String>) = apply {
        for ((k, v) in map) {
            postParamsMap[k] = v
        }
    }

    fun postJson(jsonObject: JSONObject) = apply {
        postJSONObject = jsonObject
    }

    fun uploadFile(file: File) = apply {
        uploadFile = file
    }

    fun addFormPart(key: String, value: String) = apply {
        formParamsMap[key] = value
    }


    fun addFormPart(
        key: String,
        file: File,
        mediaType: String = OkHttp.DEFAULT_MEDIA_TYPE_STRING
    ) = apply {
        formFileKeyList.add(key)
        formFileList.add(file)
        formFileMediaTypeList.add(mediaType.toMediaType())
    }

    internal fun  prepare(callBack: ResultCallBack): OkHttpClient {

        appendParamsMapToUrl(OkHttp.globalParamsMap)
        for ((k, v) in OkHttp.globalHeaderMap) {
            requestBuilder.header(k, v)
        }
        appendParamsMapToUrl(paramsMap)

        LogUtils.e(requestUrl)
        when (type) {

            "get" -> {

            }

            "post" -> {
                val formBody = FormBody.Builder()
                for ((k, v) in postParamsMap) {
                    formBody.add(k, v)
                }
                requestBuilder.post(formBody.build())

            }

            "postJson" -> {
                val requestBody =
                    postJSONObject.toString().toRequestBody("application/json".toMediaType())
                requestBuilder.post(requestBody)

            }

            "getBitmap" -> {
                val downloadBuilder = client.newBuilder()
                downloadBuilder.networkInterceptors().clear()
                downloadBuilder.interceptors().clear()
                downloadBuilder.addNetworkInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request = chain.request()
                        val url = request.url.toString()
                        val originalResponse = chain.proceed(request)
                        val originalResponseBody = originalResponse.body
                        return if (originalResponseBody == null) originalResponse else originalResponse.newBuilder()
                            .body(
                                ProgressResponseBody(
                                    url,
                                    originalResponseBody,
                                    callBack
                                )
                            ).build()

                    }
                })
                client = downloadBuilder.build()
            }

            "downloadFile" -> {
                val downloadBuilder = client.newBuilder()
                downloadBuilder.networkInterceptors().clear()
                downloadBuilder.interceptors().clear()
                client = downloadBuilder.build()
                val file = File(filePath, fileName)
                val downloadLength = if (file.exists()) file.length() else 0
                val downloadResponse = client.newCall(requestBuilder.url(requestUrl).build()).execute()
                val contentLength = downloadResponse.headersContentLength()
                val downloadBean = DownloadBean(requestUrl,fileName,filePath,contentLength,downloadLength)
                callBack.urlToBeanMap[requestUrl]=downloadBean
                if (contentLength>0){
                    requestBuilder.addHeader("RANGE", "bytes=$downloadLength-$contentLength")
                }

            }

            "uploadFile" -> {
                val downloadBuilder = client.newBuilder()
                downloadBuilder.networkInterceptors().clear()
                downloadBuilder.interceptors().clear()
                client = downloadBuilder.build()
                requestBuilder.post(
                    ProgressRequestBody(
                        uploadFile.name,
                        uploadFile.asRequestBody(defaultFileMediaType),
                        callBack
                    )
                )
            }

            "postForm" -> {
                val downloadBuilder = client.newBuilder()
                downloadBuilder.networkInterceptors().clear()
                downloadBuilder.interceptors().clear()
                client = downloadBuilder.build()
                val multipartBodyBuilder = MultipartBody.Builder()
                multipartBodyBuilder.setType(MultipartBody.FORM)
                for ((k, v) in formParamsMap) {
                    multipartBodyBuilder.addFormDataPart(k, v)
                }
                formFileKeyList.forEachIndexed { index, s ->
                    val file = formFileList[index]
                    val fileName = file.name
                    multipartBodyBuilder.addFormDataPart(
                        s,
                        fileName,
                        ProgressRequestBody(
                            fileName,
                            file.asRequestBody(formFileMediaTypeList[index]),
                            callBack
                        )
                    )
                }
                requestBuilder.post(multipartBodyBuilder.build())
            }

        }

        return client
    }
}