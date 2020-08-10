package com.example.baselibrary.widget


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.example.baselibrary.R
import com.example.baselibrary.utils.SizeUtils.dp2px
import com.example.baselibrary.utils.SizeUtils.sp2px


class CustomToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context,attrs, defStyleAttr) {
    private val MODE_IMAGE = 0
    private val MODE_TEXT = 1

    private val mContext: Context = context

    private var leftView: View? = null
    private var rightView: View? = null
    private var tvTitle: TextView? = null

    //---------------------------
    private var isLeftTextOrImageMode:Int  = MODE_IMAGE //判断左边是用图片还是文字，true为图片

    private var isRightTextOrImageMode:Int  = MODE_TEXT //判断右边是用图片还是文字，true为图片

    private var showRightView = false //右边View是否显示

    private var showLeftView = true //左边View是否显示


    private var strTitle //设置标题的文字信息
            : String? = null
    private var colorTitleText:Int = Color.BLACK //设置标题文字颜色

    private var sizeTitleText: Float = sp2px(15f) //设置标题文字大小


    private var strRightText //右边文字信息
            : String? = null
    private var colorRightText:Int  = Color.BLACK //设置右边文字的颜色

    private var imageRight //设置右边图片
            : Drawable? = null
    private var sizeRightText: Float = sp2px(10f) //设置右边文字大小

    private var imageRightWidth:Int  = dp2px(30f) //设置右边图片的宽度
        .toInt()
    private var imageRightHeight:Int  = dp2px(30f) //设置右边图片的宽度
        .toInt()

    private var strLeftText //左边文字信息
            : String? = null
    private var colorLeftText:Int  = Color.BLACK //设置左边文字的颜色

    private var imageLeft //设置右边图片
            : Drawable? = null
    private var sizeLeftText: Float = sp2px(10f) //设置标题文字大小

    private var imageLeftWidth:Int  = dp2px(30f) //设置右边图片的宽度
        .toInt()
    private var imageLeftHeight:Int  = dp2px(30f) //设置右边图片的宽度
        .toInt()

    init{
        attrs?.let { setAttrs(it) }
        if (showLeftView){
            addLeftView();
        }
        if (showRightView){
            addRightView();
        }
        addtitleView();
    }


    /**通过布局设置 */
    private fun setAttrs(attrs: AttributeSet) {
        val typedArray : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)
        strTitle = typedArray.getString(R.styleable.CustomToolBar_titleText)
        colorTitleText = typedArray.getColor(R.styleable.CustomToolBar_titleTextColor, Color.BLACK)
        sizeTitleText = typedArray.getDimension(R.styleable.CustomToolBar_titleTextSize, sp2px(15f))
        showRightView = typedArray.getBoolean(R.styleable.CustomToolBar_rightShow, false)
        isRightTextOrImageMode = typedArray.getInteger(R.styleable.CustomToolBar_rightShowView, MODE_TEXT)
        strRightText = typedArray.getString(R.styleable.CustomToolBar_rightText)
        colorRightText = typedArray.getColor(R.styleable.CustomToolBar_rightTextColor, Color.BLACK)
        imageRight = typedArray.getDrawable(R.styleable.CustomToolBar_rightImage)
        sizeRightText = typedArray.getDimension(R.styleable.CustomToolBar_rightTextSize, sp2px(10f))
        imageRightWidth =
            typedArray.getDimensionPixelSize(R.styleable.CustomToolBar_rightImageWidth, dp2px(30f).toInt())
        imageRightHeight =
            typedArray.getDimensionPixelSize(R.styleable.CustomToolBar_rightImageHeight, dp2px(30f).toInt())
        showLeftView = typedArray.getBoolean(R.styleable.CustomToolBar_leftShow, true)
        isLeftTextOrImageMode = typedArray.getInteger(R.styleable.CustomToolBar_leftShowView, MODE_IMAGE)
        strLeftText = typedArray.getString(R.styleable.CustomToolBar_leftText)
        colorLeftText = typedArray.getColor(R.styleable.CustomToolBar_leftTextColor, Color.BLACK)
        imageLeft = typedArray.getDrawable(R.styleable.CustomToolBar_leftImage)
        sizeLeftText = typedArray.getDimension(R.styleable.CustomToolBar_leftTextSize, sp2px(10f))
        imageLeftWidth =
            typedArray.getDimensionPixelSize(R.styleable.CustomToolBar_leftImageWidth, dp2px(30f).toInt())
        imageLeftHeight =
            typedArray.getDimensionPixelSize(R.styleable.CustomToolBar_leftImageHeight, dp2px(30f).toInt())
        typedArray.recycle() //回收资源
    }


    /**添加标题 */
    private fun addtitleView() {
        tvTitle = TextView(context)
        tvTitle?.setText(strTitle ?: "标题")
        tvTitle?.setGravity(Gravity.CENTER) //居中
        tvTitle?.setTextColor(colorTitleText)
        tvTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeTitleText)
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, dp2px(10f).toInt(), 0, dp2px(10f).toInt())
        layoutParams.addRule(CENTER_IN_PARENT)
        tvTitle?.setLayoutParams(layoutParams) //上面设置控件的高宽后就落实
        addView(tvTitle)
    }

    /**添加左边视图 */
    private fun addLeftView() {
        leftView = when (isLeftTextOrImageMode) {
            MODE_IMAGE -> addLeftImageView()
            MODE_TEXT -> addLeftTextView()
            else -> View(context)
        }
        addView(leftView)
    }

    private fun addRightView() {
        rightView = when (isRightTextOrImageMode) {
            MODE_IMAGE -> addRightImageView()
            MODE_TEXT -> addRightTextView()
            else -> View(context)
        }
        addView(rightView)
    }

    private fun addRightTextView(): View {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER //居中
        textView.setTextColor(Color.BLACK)

        strRightText?.let {   textView.text = strRightText  }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeRightText)
        textView.setTextColor(colorRightText)
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(ALIGN_PARENT_RIGHT)
        layoutParams.addRule(CENTER_VERTICAL)
        layoutParams.setMargins(
            0,
            dp2px(10f).toInt(),
            dp2px(10f).toInt(),
            dp2px(10f).toInt()
        )
        textView.layoutParams = layoutParams
        return textView
    }

    private fun addRightImageView(): View {
        val imageView = ImageView(context)

        imageRight?.let {   imageView.setImageDrawable(imageRight)}
        val layoutParams = LayoutParams(imageRightWidth, imageRightHeight)
        layoutParams.addRule(ALIGN_PARENT_RIGHT)
        layoutParams.addRule(CENTER_VERTICAL)
        layoutParams.setMargins(
            0,
            dp2px(10f).toInt(),
            dp2px(10f).toInt(),
            dp2px(10f).toInt()
        )
        imageView.layoutParams = layoutParams
        return imageView
    }

    /**添加左边文字 */
    private fun addLeftTextView(): View {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER //居中
        textView.setTextColor(Color.BLACK)
        strLeftText?.let {  textView.text = strLeftText }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeLeftText)
        textView.setTextColor(colorLeftText)
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(CENTER_VERTICAL)
        layoutParams.setMargins(
            dp2px(10f).toInt(),
            dp2px(10f).toInt(),
            0,
            dp2px(10f).toInt()
        )
        textView.layoutParams = layoutParams //上面设置控件的高宽后就落实
        return textView
    }

    /**添加左边图片 */
    private fun addLeftImageView(): View {
        val imageView = ImageView(context)
        imageLeft?.let {    imageView.setImageDrawable(imageLeft) }
        val layoutParams = LayoutParams(imageLeftWidth, imageLeftHeight)
        layoutParams.addRule(ALIGN_PARENT_LEFT)
        layoutParams.addRule(CENTER_VERTICAL)
        layoutParams.setMargins(
            dp2px(10f).toInt(),
            dp2px(10f).toInt(),
            0,
            dp2px(10f).toInt()
        )
        imageView.layoutParams = layoutParams
        return imageView
    }
    //-------------公共方法调取--------------------
    /**设置标题名称 */
    fun setTitle(text: CharSequence?) {
      tvTitle?.text = text
    }

    /**设置左边的文字 */
    fun setLeftText(text: CharSequence?) {
        if (showLeftView) { //判断文字是否显示
            if (isLeftTextOrImageMode === MODE_TEXT) {
                text?.let {
                    val textView:TextView = leftView as TextView
                    textView.text = text
                }

            }
        }
    }

    /**设置左边的图片 */
    fun setLeftImage(@DrawableRes res: Int) {
        if (showLeftView) { //判断文字是否显示
            if (isLeftTextOrImageMode === MODE_IMAGE) {
                val imageView:ImageView = leftView as ImageView
                imageView.setImageResource(res)
            }
        }
    }

    /**设置左边的图片 */
    fun setLeftImage(drawable: Drawable?) {
        if (showLeftView) { //判断文字是否显示
            if (isLeftTextOrImageMode === MODE_IMAGE) {
                drawable?.let {
                    val imageView:ImageView = leftView as ImageView
                    imageView.setImageDrawable(drawable)
                }

            }
        }
    }

    /**设置右边的文字 */
    fun setRightText(text: CharSequence?) {
        if (showRightView) { //判断文字是否显示
            if (isRightTextOrImageMode === MODE_TEXT) {
                val textView:TextView  = rightView as TextView
                textView.text = text
            }
        }
    }

    /**设置右边的图片 */
    fun setRightImage(@DrawableRes res: Int) {
        if (showRightView) { //判断文字是否显示
            if (isRightTextOrImageMode === MODE_IMAGE) {
                val imageView:ImageView = rightView as ImageView
                imageView.setImageResource(res)
            }
        }
    }

    /**设置右边的图片 */
    fun setRightImage(drawable: Drawable?) {
        if (showRightView) { //判断文字是否显示
            if (isRightTextOrImageMode === MODE_IMAGE) {
                drawable?.let {
                    val imageView:ImageView = rightView as ImageView
                    imageView.setImageDrawable(drawable)
                }
            }
        }
    }

    /**设置左边的单击监听 */
    fun setOnLeftClickListener(listener: ((View) -> Unit)?) {//使用lambda表达式
       // leftView?.setOnClickListener(listener);
        leftView?.setOnClickListener { v -> listener?.invoke(v) }
    }

    /**设置右边的单击监听 */
    fun setOnRightClickListener(listener:((View) -> Unit)?) {
//        rightView?.setOnClickListener(listener);
        rightView?.setOnClickListener {  v -> listener?.invoke(v) }
    }

    //-------------接口--------------------

    //左边的监听接口
    interface OnLeftClickListener : OnClickListener

    //右边的监听接口
    interface OnRightClickListener : OnClickListener
}