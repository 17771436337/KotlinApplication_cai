package com.example.myapplication.mvp.user.login
import android.text.TextUtils
import com.example.baselibrary.mvp.presenter.BasePresenter

class LoginPresenter : BasePresenter<LoginView>(){


    fun login(){
        if (TextUtils.isEmpty(mView.getUserName())) {
            mView.showToast("请输入用户名")
            return
        }

        if (TextUtils.isEmpty(mView.getPassWord())) {
            mView.showToast("请输入密码")
            return
        }
        mView.showToast("登录成功" + mView.getPassWord())
    }
}