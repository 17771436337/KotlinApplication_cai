package com.example.baselibrary.mvp.presenter

import android.content.Context
import com.example.baselibrary.mvp.view.IView

abstract class BasePresenter <out View : IView<BasePresenter<View>>> : IPresenter<View> {
    override lateinit var mView: @UnsafeVariance View


}