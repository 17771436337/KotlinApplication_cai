package com.example.baselibrary.mvp.presenter

import android.content.Context
import com.example.baselibrary.mvp.view.IView

interface IPresenter<out View : IView<IPresenter<View>>> {
    val mView: View

}