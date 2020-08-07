package com.example.myapplication.mvp.user.forgot

import android.text.TextUtils
import com.example.baselibrary.mvp.presenter.BasePresenter

class ForgotPresenter : BasePresenter<ForgotView>(){

    fun confirm(){
        if (TextUtils.isEmpty(mView.getUserName())) {
            mView.showToast("请输入用户名")
            return
        }

        if (TextUtils.isEmpty(mView.getPassWord())) {
            mView.showToast("请输入密码")
            return
        }
        if (TextUtils.isEmpty(mView.getOldPassWord())) {
            mView.showToast("请输入确认密码")
            return
        }

        if (!mView.getOldPassWord().equals(mView.getPassWord())){
            mView.showToast("请确保两次密码一致")
            return
        }

        mView.showToast("修改成功" + mView.getPassWord())
    }
}