package com.example.myapplication.mvp.user.register

import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_forgot.btn_confirm
import kotlinx.android.synthetic.main.activity_forgot.et3
import kotlinx.android.synthetic.main.activity_login.bt1
import kotlinx.android.synthetic.main.activity_login.bt2
import kotlinx.android.synthetic.main.activity_login.et1
import kotlinx.android.synthetic.main.activity_login.et2
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.view_title_no_right.*

class RegisterActivity : BaseMvpActivity<RegisterPresenter,Any>(),RegisterView{
    override fun getContentView(): Int {
        return R.layout.activity_register
    }
    override fun onSuccess(url: String, data: Any?) {
        TODO("Not yet implemented")
    }
    override fun init() {
        tv_title.text = "注册界面"

        iv_back.setOnClickListener {//返回
            finish()
        }
        bt1?.setOnClickListener {//
            et1?.setText("")

        }
        bt2?.setOnClickListener {//
            et1?.setText("")
            et2?.setText("")
        }

        code_view.setOnClickListener {//发送验证码
            mPresenter.sendCode()
        }

        btn_confirm.setOnClickListener {//确定注册
          mPresenter.register();
        }

    }

    override fun getUserName(): String {
        return et1.text.toString().trim()
    }

    override fun getPassWord(): String {
        return et2.text.toString().trim()
    }

    override fun getCode(): String {
        return et3.text.toString().trim()
    }

    override fun setCode(code: String) {
        code_view.setCode(code)
    }
}