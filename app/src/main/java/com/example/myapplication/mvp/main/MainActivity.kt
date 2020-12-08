package com.example.myapplication.mvp.main
import android.util.Log
import com.example.baselibrary.anno.CreateHttp
import com.example.baselibrary.anno.HttpType
import com.example.baselibrary.mvp.view.base.BaseMvpActivity
import com.example.baselibrary.utils.GsonUtils
import com.example.baselibrary.utils.LogUtils
import com.example.baselibrary.utils.ThreadUtils
import com.example.myapplication.DataBase
import com.example.myapplication.R.layout.activity_main
import com.example.myapplication.http.HttpApi
import com.example.myapplication.http.LIST
import com.example.myapplication.http.XINWEN
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
class MainActivity : BaseMvpActivity<MainPresenter>() , MainView {

    private val testTask = TestTask()

    public override fun getContentView(): Int { return activity_main }


    @CreateHttp(type = HttpType.OK,url = XINWEN)
    fun  onSuccess(data: String) = try {
   // showToast("代码测试成功11:${data}")

       val bean = GsonUtils.fromJson(data,DataBase::class.java)

            showToast("代码测试成功:${bean?.reason}")
        }catch (e:Exception){
            showToast("转换错误：${e.message}")
            LogUtils.e("转换错误：${e.message}")
        }

    @CreateHttp(type = HttpType.OK,url = LIST)
    fun onSuccessList(data:String){
        showToast("代码测试成功:${data}")
    }


    override fun init() {

        val layoutIds = intArrayOf(activity_main, activity_main)

//        val adapter = Test<DataBaseBean>(this,layoutIds)
        title_view.setOnRightClickListener {
         //  v ->  showToast("开发中" + (v as TextView) .text)
//            testTask.execute()
            HttpApi.setXinWen()
        }

        test?.setOnClickListener {
//           val random = (0x1000..0xFFFE).random()
//            test.setCode(random.toString())
            HttpApi.sendCmdList()
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
