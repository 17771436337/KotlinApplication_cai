package com.example.myapplication.mvp.user.register

import com.example.baselibrary.mvp.view.BaseView

interface RegisterView : BaseView<RegisterPresenter>{


    /**用户名字*/
    fun getUserName() : String ;

    /**用户名字*/
    fun getPassWord() : String ;

    /**验证码*/
    fun getCode():String

    /**设置验证码*/
    fun setCode(code:String)
}