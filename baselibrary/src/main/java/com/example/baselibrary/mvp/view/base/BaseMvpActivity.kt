package com.example.baselibrary.mvp.view.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.baselibrary.anno.HttpCallBack
import com.example.baselibrary.network.model.HttpMseeageEvent
import com.example.baselibrary.mvp.Constant
import com.example.baselibrary.mvp.presenter.BasePresenter
import com.example.baselibrary.mvp.view.BaseView
import com.example.baselibrary.utils.LogUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

/**基础Activity*/
 abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>> : AppCompatActivity(),BaseView<Presenter> {

    final override val mPresenter: Presenter
    init {
        mPresenter = findPresenterClass().newInstance()
        this.also { mPresenter.mView = it }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getContentView() != Constant.USE_SELEF_VIEW) {//判断是否使用自己得View
            setContentView(getContentView())
        }
        preInit(savedInstanceState)
        EventBus.getDefault().register(this)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<Presenter>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show()
    }

    override fun getContext(): Context {
        return this;
    }

    override fun showLoading(color: Int, tip: String) {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    /*************************************************************/
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN )
    fun HttpStickyEvent(messageEvent: HttpMseeageEvent) {
        getHttp(this,messageEvent)

    }


    //-------------------------------

    //获取函数上面的注解
    private fun getHttp(cls:Activity,messageEvent: HttpMseeageEvent) {
        val clazz =  cls::class.java
        val annotations = clazz.declaredMethods
        for (i in annotations) {
            val annotation = i.getAnnotation(HttpCallBack::class.java)
            annotation?.let {
                LogUtils.e("函数注解的值：${it.type.name},${it.url},${messageEvent.code},${messageEvent.url}")
                if (messageEvent.code == it.type && it.url == messageEvent.url){
                    LogUtils.e("函数注解的值：${i.name},${i.modifiers},${messageEvent.url},${messageEvent.data},${messageEvent.code}")
                    try{
                        i(cls,messageEvent.data)
                    }catch (e:Exception){
                        LogUtils.e("错误日志：${e.message}")
                    }
                }
            }
        }
    }




    /*****************************分割线**************************************/
    /**
     * 在init方法之前 用于getContentView == -1的情况
     * @param savedInstanceState
     */
   protected fun preInit(savedInstanceState: Bundle?){}
    /*****************************分割线**************************************/
    /**返回资源布局id*/
   protected abstract fun getContentView():Int @SuppressLint("SupportAnnotationUsage")  @LayoutRes
   protected abstract fun init()

}