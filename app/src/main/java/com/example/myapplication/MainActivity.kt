package com.example.myapplication
import android.widget.TextView
import android.widget.Toast
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
class MainActivity : BaseMvpActivity<MainPresenter>() ,MainView{
    var tv : TextView? = null;

    public override fun getContentView(): Int { return R.layout.activity_main }

    override fun onMainSuccess(s: String) {
       Toast.makeText(mContext,"测试",Toast.LENGTH_SHORT).show()
        tv?.setText(s+"");
    }

    override fun init() {
        tv = findViewById(R.id.tv);
        tv?.setOnClickListener{
            mPresenter.getData()
        }
    }
}
