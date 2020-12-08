package com.example.baselibrary.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import com.example.baselibrary.network.okhttp.OkHttp

object NetworkUtil{
    var connectivityManager: ConnectivityManager? = null
    fun init(application: Application) {
        connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val manager = connectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager?.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        OkHttp.networkAvailable = true
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        OkHttp.networkAvailable = false
                    }
                })
        }

    }

    fun isNetworkAvailable(): Boolean {
        val manager= connectivityManager
        return  if (manager==null) true else OkHttp.networkAvailable
    }

}