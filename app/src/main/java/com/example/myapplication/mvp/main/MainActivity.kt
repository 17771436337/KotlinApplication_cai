package com.example.myapplication.mvp.main
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.baselibrary.adapter.LayoutWrapper
import com.example.baselibrary.adapter.SimpleRecyclerHolder
import com.example.baselibrary.adapter.SingleAdapter
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.baselibrary.widget.CustomToolBar
import com.example.baselibrary.widget.CustomToolBar.OnLeftClickListener
import com.example.myapplication.DataBaseBean
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import com.example.baselibrary.adapter.MultiAdapter as MultiAdapter


class MainActivity : BaseMvpActivity<MainPresenter>() , MainView {

    public override fun getContentView(): Int { return R.layout.activity_main }

    override fun onMainSuccess(s: String) {
        showToast("测试")

    }

    override fun init() {

        var layoutIds: IntArray = intArrayOf(R.layout.activity_main, R.layout.activity_main)

        var adapter = Test<DataBaseBean>(this,layoutIds)
        title_view.setOnRightClickListener {
           v ->  showToast("开发中" + (v as TextView) .text)
        }

        test?.setOnClickListener {
           val random = (0x1000..0xFFFE).random()
            test.setCode(random.toString())
        }
    }


    class Test<T> constructor(context:Context,layoutIds:IntArray) : MultiAdapter<T>(context,layoutIds){
        override fun bindLayout(item: T, viewType: Int): Int {
           return R.layout.activity_main
        }

        override fun bindData(context: Context?, holder: SimpleRecyclerHolder, item: T, layoutId: Int, position: Int) {

        }

    }

}
