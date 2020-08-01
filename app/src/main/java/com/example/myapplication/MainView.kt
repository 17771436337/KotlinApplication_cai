package com.example.myapplication

import com.example.baselibrary.mvp.view.BaseView

interface MainView :BaseView<MainPresenter> {
    fun onMainSuccess(s: String)
}