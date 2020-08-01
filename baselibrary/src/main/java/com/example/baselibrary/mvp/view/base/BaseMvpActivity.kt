package com.example.baselibrary.mvp.view.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.baselibrary.mvp.Constant
import com.example.baselibrary.mvp.presenter.BasePresenter
import com.example.baselibrary.mvp.view.BaseView
import java.lang.reflect.ParameterizedType



/**基础Activity*/
public abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>> : AppCompatActivity(),BaseView<Presenter> {
    protected var mContext: Context? = null;
    final override val mPresenter: Presenter
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
        mContext = this;
        init();
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
        TODO("Not yet implemented")
    }

    override fun showLoading(color: Int, tip: String) {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun onError(throwable: Throwable) {
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
   protected abstract fun getContentView():Int
    protected abstract fun init();

}