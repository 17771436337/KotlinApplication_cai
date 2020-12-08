package com.example.baselibrary.mvp.presenter

import com.example.baselibrary.network.other.HttpManager
import com.example.baselibrary.mvp.view.IView

abstract class BasePresenter <out View : IView<BasePresenter<View>>> : IPresenter<View> {
    override lateinit var mView: @UnsafeVariance View

    protected var httpManager : HttpManager = HttpManager.INSTANCE

}