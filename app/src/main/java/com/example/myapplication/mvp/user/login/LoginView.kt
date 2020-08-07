package com.example.myapplication.mvp.user.login

import com.example.baselibrary.mvp.view.BaseView

interface LoginView : BaseView<LoginPresenter> {

    /**用户名字*/
    fun getUserName() : String ;

    /**用户名字*/
    fun getPassWord() : String ;
}