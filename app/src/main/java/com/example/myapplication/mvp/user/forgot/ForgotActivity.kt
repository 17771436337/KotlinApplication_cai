package com.example.myapplication.mvp.user.forgot

import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_login.bt1
import kotlinx.android.synthetic.main.activity_login.bt2
import kotlinx.android.synthetic.main.activity_login.et1
import kotlinx.android.synthetic.main.activity_login.et2
import kotlinx.android.synthetic.main.view_title_no_right.*

class ForgotActivity :BaseMvpActivity<ForgotPresenter>() ,ForgotView{
    override fun getContentView(): Int {
        return R.layout.activity_forgot
    }



    override fun init() {
        tv_title.text = "修改密码界面"

        iv_back.setOnClickListener {//返回
            finish()
        }

        bt1?.setOnClickListener {//
            et1?.setText("")

        }
        bt2?.setOnClickListener {//
            et1?.setText("")
            et2?.setText("")
            et3.setText("")

        }
        bt3.setOnClickListener {
            et2?.setText("")
            et3.setText("")
        }

        btn_confirm.setOnClickListener {//确定修改密码
            mPresenter.confirm()

        }
    }

    override fun getUserName(): String {
        return et1.text.toString().trim()
    }

    override fun getPassWord(): String {
        return et2.text.toString().trim()
    }

    override fun getOldPassWord(): String {
        return et3.text.toString().trim()
    }


}