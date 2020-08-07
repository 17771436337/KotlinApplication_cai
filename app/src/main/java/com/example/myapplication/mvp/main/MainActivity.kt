package com.example.myapplication.mvp.main
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<MainPresenter>() , MainView {

    public override fun getContentView(): Int { return R.layout.activity_main }

    override fun onMainSuccess(s: String) {
        showToast("测试")
        tv.text = s+""
    }

    override fun init() {
        tv?.setOnClickListener{
            mPresenter.getData()
        }
        test?.setOnClickListener {
           val random = (0x1000..0xFFFE).random()
            test.setCode(random.toString())
        }
    }
}
