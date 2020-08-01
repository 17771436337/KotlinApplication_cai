package com.example.baselibrary.mvp.view

import android.graphics.Color
import com.example.baselibrary.mvp.presenter.IPresenter

interface IView <out Presenter : IPresenter<IView<Presenter>>>{
    val mPresenter: Presenter

    fun showToast(msg: String)

    fun showLoading(color: Int = Color.BLUE, tip: String = " 正在加载中 ... ")

    fun hideLoading()

    fun onError(throwable: Throwable)
}