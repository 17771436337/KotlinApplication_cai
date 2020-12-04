package com.example.baselibrary.mvp.view.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.baselibrary.http.HttpHandler
import com.example.baselibrary.http.HttpView
import com.example.baselibrary.mvp.Constant
import com.example.baselibrary.mvp.presenter.BasePresenter
import com.example.baselibrary.mvp.view.BaseView
import java.lang.reflect.ParameterizedType



/**基础Activity*/
 abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>,Model> : AppCompatActivity(),BaseView<Presenter>,HttpView<Model> {

    final override val mPresenter: Presenter

    /**消息接收机制*/
    private var httpHandler:HttpHandler<Model>?= HttpHandler<Model>(this)

    init {
        mPresenter = findPresenterClass().newInstance()
        mPresenter.mView = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getContentView() != Constant.USE_SELEF_VIEW) {//判断是否使用自己得View
            setContentView(getContentView())
        }
        preInit(savedInstanceState);
        httpHandler?.registered();
        init();
    }

    override fun onDestroy() {
        super.onDestroy()
        httpHandler?.unRegistered();
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass;
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

    override fun onError(url: String, e: Exception?) {
        TODO("Not yet implemented")
    }



    /*****************************分割线**************************************/
    /**
     * 在init方法之前 用于getContentView == -1的情况
     * @param savedInstanceState
     */
    fun preInit(savedInstanceState: Bundle?){

    }
    /*****************************分割线**************************************/
    /**返回资源布局id*/
   protected abstract fun getContentView():Int @SuppressLint("SupportAnnotationUsage")
    @LayoutRes
    protected abstract fun init();

}