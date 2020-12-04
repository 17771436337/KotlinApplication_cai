package com.example.myapplication.mvp.user.login


import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.myapplication.R
import com.example.myapplication.mvp.user.forgot.ForgotActivity
import com.example.myapplication.mvp.user.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity() : BaseMvpActivity<LoginPresenter,Any>(),LoginView, Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun getContentView(): Int {
        return R.layout.activity_login;
    }

    override fun init() {
        btn_login?.setOnClickListener {//登录
            mPresenter.login()
        }
        forgot?.setOnClickListener {//忘记密码
            var intent : Intent = Intent(getContext(),ForgotActivity::class.java)
            startActivity(intent)

        }
        registered?.setOnClickListener {//注册
            var intent : Intent = Intent(getContext(),RegisterActivity::class.java)
            startActivity(intent)
        }
        bt1?.setOnClickListener {//
            et1?.setText("")

        }
        bt2?.setOnClickListener {//
            et1?.setText("")
            et2?.setText("")

        }

    }



    //------------------------
    override fun getUserName(): String {
        return et1?.text.toString().trim();
    }

    override fun getPassWord(): String {
        return et2?.text.toString().trim();
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginActivity> {
        override fun createFromParcel(parcel: Parcel): LoginActivity {
            return LoginActivity(parcel)
        }

        override fun newArray(size: Int): Array<LoginActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onSuccess(url: String, data: Any?) {
        TODO("Not yet implemented")
    }


}