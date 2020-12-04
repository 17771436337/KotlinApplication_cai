package com.example.myapplication.mvp.main
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.baselibrary.adapter.LayoutWrapper
import com.example.baselibrary.adapter.SimpleRecyclerHolder
import com.example.baselibrary.adapter.SingleAdapter
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.baselibrary.utils.LogUtils
import com.example.baselibrary.utils.ThreadUtils
import com.example.baselibrary.widget.CustomToolBar
import com.example.baselibrary.widget.CustomToolBar.OnLeftClickListener
import com.example.myapplication.DataBaseBean
import com.example.myapplication.R
import com.example.myapplication.http.HttpApi
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import com.example.baselibrary.adapter.MultiAdapter as MultiAdapter


class MainActivity : BaseMvpActivity<MainPresenter,DataBaseBean>() , MainView {

    private var testTask :TestTask = TestTask()

    public override fun getContentView(): Int { return R.layout.activity_main }


    override fun onSuccess(url: String, data: DataBaseBean?) {
        showToast("测试:"+url)
    }




    override fun init() {

        var layoutIds: IntArray = intArrayOf(R.layout.activity_main, R.layout.activity_main)

        var adapter = Test<DataBaseBean>(this,layoutIds)
        title_view.setOnRightClickListener {
         //  v ->  showToast("开发中" + (v as TextView) .text)
//            testTask.execute()
            HttpApi.sendCmdList();
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


    class TestTask : ThreadUtils.Task<String>(){
        override fun doInBackground(): String {
            return "这是一个测试线程"
        }

        override fun onSuccess(result: String) {
            Log.d("TAG",""+result)
        }

        override fun onCancel() {
            ThreadUtils.cancel(this)
        }

        override fun onFail(t: Throwable?) {
            Log.d("TAG","线程出错")
        }

        fun execute(){
            ThreadUtils.executeByCpuAtFixRate(this,100L,TimeUnit.MILLISECONDS)
        }

    }




}
