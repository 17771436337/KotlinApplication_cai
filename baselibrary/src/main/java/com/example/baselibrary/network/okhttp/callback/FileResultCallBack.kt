package com.example.baselibrary.network.okhttp.callback

import com.example.baselibrary.network.model.DownloadBean
import com.example.baselibrary.network.okhttp.OkHttp
import okhttp3.Call
import okhttp3.Response
import java.io.File
import java.io.RandomAccessFile
abstract class FileResultCallBack : ResultCallBack() {
    override fun start() {

    }
    override fun response(call: Call, response: Response) {
        val responseBody = response.body
        if (responseBody==null){
            responseBodyGetNull(call, response)
            return
        }
        try {
            val url=response.request.url.toString()
            val bean= urlToBeanMap[url]?.let {
                DownloadBean(it.url,it.filename,it.filePath,it.downloadLength,it.contentLength)
            }
            val inputStream = responseBody.byteStream()
            val file = File(bean?.filePath, bean?.filename)
            val randomAccessFile= RandomAccessFile(file,"rw")
            randomAccessFile.seek(bean?.downloadLength?:0)
            inputStream.use { input ->
                var bytesCopied: Long = bean?.downloadLength ?:0
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytes = input.read(buffer)
                while (bytes >= 0) {
                    randomAccessFile.write(buffer,0,bytes)
                    bytesCopied += bytes
                    bytes = input.read(buffer)
                    downloadProgress(url,bean?.contentLength?:0,bytesCopied)
                }
            }
            randomAccessFile.close()
            OkHttp.mainHandler.post {
                finish(file)
            }
        }catch (e:Exception){
            OkHttp.mainHandler.post {
                otherException(call, response, e)
            }
        }
    }

    override fun otherException(call: Call, response: Response, e: Exception) {
        failure(call,e)
    }

    override fun responseBodyGetNull(call: Call, response: Response) {

    }

    override fun downloadProgress(url:String,total: Long, current: Long) {
        OkHttp.mainHandler.post {
            downloadProgressOnMainThread(url,total, current)
        }
    }

    override fun uploadProgress(fileName: String, total: Long, current: Long) {}

    override fun uploadProgressOnMainThread(fileName: String, total: Long, current: Long) {}

    abstract fun finish(file:File)
}