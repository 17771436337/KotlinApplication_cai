package com.example.myapplication.mvp.user.register

import android.text.TextUtils
import com.example.baselibrary.mvp.presenter.BasePresenter

class RegisterPresenter : BasePresenter<RegisterView>() {

    /**注册*/
    fun register(){
        if (TextUtils.isEmpty(mView.getUserName())) {
            mView.showToast("请输入用户名")
            return
        }

        if (TextUtils.isEmpty(mView.getPassWord())) {
            mView.showToast("请输入密码")
            return
        }
        if (TextUtils.isEmpty(mView.getCode())) {
            mView.showToast("请输入验证码")
            return
        }
        mView.showToast("注册成功" + mView.getPassWord())

    }

    /**发送验证码*/
    fun sendCode(){
        if (TextUtils.isEmpty(mView.getUserName())) {
            mView.showToast("请输入用户名")
            return
        }
        mView.showToast("验证码成功" + mView.getPassWord())

        val random = (0x1000..0xFFFE).random()
        mView.setCode(random.toString())
    }
}