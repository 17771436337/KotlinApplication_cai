package com.example.baselibrary.mvp.view

import com.example.baselibrary.mvp.presenter.BasePresenter

 interface BaseView <out Presenter : BasePresenter<BaseView<Presenter>>> : IView<Presenter>
