package com.example.myapplication.mvp.user.forgot

import com.example.baselibrary.mvp.view.BaseView

interface ForgotView : BaseView<ForgotPresenter> {


    /**用户名字*/
    fun getUserName() : String ;

    /**用户名字*/
    fun getPassWord() : String ;

    /**用户名字*/
    fun getOldPassWord() : String ;
}