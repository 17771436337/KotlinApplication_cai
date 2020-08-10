package com.example.baselibrary.adapter

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class SimpleRecyclerHolder constructor( context: Context, itemView: View) :RecyclerView.ViewHolder(itemView) {
    private var mCtx: Context = context
    private var mViewArray = SparseArray<View>()


    fun getItemView(): View {
        return itemView
    }

    fun getContext(): Context {
        return mCtx
    }

    fun getViewById(@IdRes viewId: Int): View {
        return retrieveView<View>(viewId)
    }

    /**
     * 整个item的点击事件
     */
    fun setOnItemClickListenner(listener: OnClickListener?): SimpleRecyclerHolder {
        getItemView().setOnClickListener(listener)
        return this
    }

    /**
     * 整个item的点击事件，可以根据条件来禁止某些符合条件的点击事件
     *
     *
     * 这个需求是在是开发的时候发现的，才加上去的，能不能点击根据后端的返回来确定的，可以使用该方法
     * 比如：列表显示了很多好友的用户名，在线的可以点击，不在线的不能点击，
     */
    fun setOnItemClickListenner(
        isListener: Boolean,
        listener: OnClickListener?
    ): SimpleRecyclerHolder {
        getItemView().setOnClickListener(if (isListener) listener else null)
        return this
    }

    /**
     * 整个item的长按事件
     */
    fun setOnItemLongClickListener(listener: OnLongClickListener?): SimpleRecyclerHolder {
        getItemView().setOnLongClickListener(listener)
        return this
    }

    fun setOnItemLongClickListener(
        isListener: Boolean,
        listener: OnLongClickListener?
    ): SimpleRecyclerHolder {
        getItemView().setOnLongClickListener(if (isListener) listener else null)
        return this
    }

    /**
     * 整个item的触摸事件
     */
    fun setOnItemTouchListener(listener: OnTouchListener?): SimpleRecyclerHolder {
        getItemView().setOnTouchListener(listener)
        return this
    }

    fun setOnItemTouchListener(
        isListener: Boolean,
        listener: OnTouchListener?
    ): SimpleRecyclerHolder{
        getItemView().setOnTouchListener(if (isListener) listener else null)
        return this
    }

    fun setOnClickListenner(
        @IdRes viewId: Int,
        listener: OnClickListener?
    ): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnClickListener(listener)
        return this
    }

    fun setOnLongClickListener(
        @IdRes viewId: Int,
        listener: OnLongClickListener?
    ): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnLongClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        @IdRes viewId: Int,
        listener: OnTouchListener?
    ): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setOnTouchListener(listener)
        return this
    }

    fun setOnFocusChangeListener(
        @IdRes viewId: Int,
        listener: OnFocusChangeListener?
    ): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.onFocusChangeListener = listener
        return this
    }

    fun setOnFocusChangeListener(
        listener: OnFocusChangeListener?
    ): SimpleRecyclerHolder {
        getItemView().onFocusChangeListener = listener
        return this
    }

    fun setAlpha(
        @IdRes viewId: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.alpha = alpha
        return this
    }

    fun setBackgroundResource(@IdRes viewId: Int, @DrawableRes resId: Int): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setBackgroundResource(resId)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setBackgroundColor(color)
        return this
    }

    fun setClickable(@IdRes viewId: Int, clickable: Boolean): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.isClickable = clickable
        return this
    }

    fun setEnabled(@IdRes viewId: Int, enabled: Boolean): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.isEnabled = enabled
        return this
    }

    fun setFocusable(@IdRes viewId: Int, focusable: Boolean): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.isFocusable = focusable
        return this
    }

    fun setFocusableInTouchMode(
        @IdRes viewId: Int,
        focusableInTouchMode: Boolean
    ): SimpleRecyclerHolder{
        retrieveView<View>(viewId)!!.isFocusableInTouchMode = focusableInTouchMode
        return this
    }

    fun setTag(@IdRes viewId: Int, tag: Any?): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.tag = tag
        return this
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any?): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.setTag(key, tag)
        return this
    }

    fun setVisibility(@IdRes viewId: Int, visibility: Int): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.visibility = visibility
        return this
    }

    /**
     * 传入是否显示，true显示，false Gone掉
     */
    fun setVisibility(@IdRes viewId: Int, isVisibility: Boolean): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.visibility = if (isVisibility) VISIBLE else GONE
        return this
    }

    fun setLongClickable(@IdRes viewId: Int, longClickable: Boolean): SimpleRecyclerHolder {
        retrieveView<View>(viewId)!!.isLongClickable = longClickable
        return this
    }

    /**
     * AppCompatCheckBox, AppCompatCheckedTextView, AppCompatRadioButton,CheckBox, CheckedTextView,
     * CompoundButton, RadioButton, Switch, SwitchCompat, ToggleButton都可以使用
     *
     * 最常使用的是CheckBox，RadioButton,SwitchCompat设置check
     */
//    fun setChecked(@IdRes viewId: Int, checked: Boolean): SimpleRecyclerHolder {
//        try {
//            val iCheckable: CheckedTextView = retrieveView<CheckedTextView>(viewId)
//            iCheckable.isChecked = checked
//            return this
//        }catch ( e:Exception){
//
//            val iCheckable: CompoundButton = retrieveView<CompoundButton>(viewId)
//            iCheckable.isChecked = checked
//            return this
//
//            Log.d("测试","错误："+e.message)
//        }
//
//
////        CheckedTextView   TextView
////        CompoundButton   Button  TextView
//
//    }

    /**
     * CheckBox, RadioButton, Switch, SwitchCompat, ToggleButton
     * ,AppCompatCheckBox, AppCompatRadioButton等都可以使用
     * 凡是继承CompoundButton的都可以使用
     *
     * 最常使用的是CheckBox，RadioButton,SwitchCompat设置监听
     */
    fun setOnCheckedChangeListener(
        @IdRes viewId: Int,
        onCheckedChangeListener: CompoundButton.OnCheckedChangeListener?
    ): SimpleRecyclerHolder {
        val checkBox = retrieveView<CompoundButton>(viewId)!!
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener)
        return this
    }

    /**
     * 该方法使用频率非常高，而且大多时候，是从网络加载的数据，所有可能会出现空指针异常
     * StringUtils.obtainNoNullText转换以后，确保不会出现空指针异常，网络请求的数据不需要再次进行判空操作
     *
     * 省去每次都要TextUtils.isEmpty操作，不用关心数据是否为空
     */
    fun setText(@IdRes viewId: Int, content: String?): SimpleRecyclerHolder {
        return setText(viewId, content, "")
    }

    /**
     * 该方法使用频率非常高，而且大多时候，是从网络加载的数据，所有可能会出现空指针异常
     * StringUtils.obtainNoNullText转换以后，确保不会出现空指针异常，网络请求的数据不需要再次进行判空操作
     *
     * 省去每次都要TextUtils.isEmpty操作，不用关心数据是否为空,可设置默认值
     */
    fun setText(@IdRes viewId: Int, content: String?, defaultContent: String?): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        if (!TextUtils.isEmpty(content)) {
            textView.text = content
        } else {
            if (!TextUtils.isEmpty(defaultContent)) {
                textView.text = defaultContent
            } else {
                textView.text = ""
            }
        }
        return this
    }

    fun setText(@IdRes viewId: Int, @StringRes resId: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setText(resId)
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextColor(color)
        return this
    }

    /**
     * 设置颜色，直接传入colorRes，在方法内部去转换
     */
    fun setTextColorResource(@IdRes viewId: Int, @ColorRes colorRes: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextColor(ContextCompat.getColor(getContext(), colorRes))
        return this
    }

    fun setTextSize(@IdRes viewId: Int, size: Float): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.textSize = size
        return this
    }

    fun setTextSize(@IdRes viewId: Int, unit: Int, size: Float): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setTextSize(unit, size)
        return this
    }

    fun setMaxLines(@IdRes viewId: Int, maxLines: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.maxLines = maxLines
        return this
    }

    fun setInputType(@IdRes viewId: Int, type: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.inputType = type
        return this
    }

    fun setHint(@IdRes viewId: Int, @StringRes resId: Int): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.setHint(resId)
        return this
    }

    fun setHint(@IdRes viewId: Int, hint: String?): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.hint = hint
        return this
    }

    fun addTextChangedListener(@IdRes viewId: Int, watcher: TextWatcher?): SimpleRecyclerHolder {
        val textView = retrieveView<TextView>(viewId)!!
        textView.addTextChangedListener(watcher)
        return this
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): SimpleRecyclerHolder {
        val imageView: ImageView = retrieveView(viewId)
        imageView.setImageBitmap(bitmap)
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int): SimpleRecyclerHolder {
        val imageView: ImageView = retrieveView(viewId)
        imageView.setImageResource(resId)
        return this
    }

    /**
     * 保留方法,自己根据项目进行修改
     * 也可以添加设置圆角的图片url等等，根据需求添加
     */
    fun setImageUrl(@IdRes viewId: Int, url: String?): SimpleRecyclerHolder {
        if (TextUtils.isEmpty(url)) {
            return this
        }
        val imageView: ImageView = retrieveView(viewId)
        //TODO 请根据自己项目使用的图片加载框架来加载
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): SimpleRecyclerHolder {
        val progressBar = retrieveView<ProgressBar>(viewId)!!
        progressBar.progress = progress
        return this
    }

    fun setProgressMax(@IdRes viewId: Int, max: Int): SimpleRecyclerHolder {
        val progressBar = retrieveView<ProgressBar>(viewId)!!
        progressBar.max = max
        return this
    }

    /**
     * 通过viewId从缓存中获取View
     *
     *
     * 对View进行缓存处理
     */
    private fun <T : View> retrieveView(@IdRes viewId: Int): T {
        var retrieveView :View = mViewArray[viewId]
        if (retrieveView == null) {
            retrieveView = getItemView().findViewById(viewId)
            mViewArray.put(viewId, retrieveView)
        }
        return retrieveView as T
    }



    /**静态语法块*/
    companion object {
        /**
         * 创建ViewHolder实例
         */
        fun createViewHolder(ctx: Context, itemView: View): SimpleRecyclerHolder {
            return SimpleRecyclerHolder(ctx, itemView)
        }
    }
}