package com.example.myapplication.utils.widget


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.example.baselibrary.utils.SizeUtils

/**自定义图片验证码*/
class VerifyCodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)  : View(context,attrs, defStyleAttr){

    private var codePaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)//验证码画笔
    private var linePaint : Paint = Paint()//干扰线绘画
    private var checkCodeText = ""//验证码文字
    private var borderPaint : Paint = Paint()//边框画笔

    private var colorCode: Int = Color.RED//验证码绘画的颜色
    private var colorLine: Int = Color.GRAY//干扰线绘画的颜色
    private val colorBorder : Int =  Color.BLACK//边框绘画的颜色

    private var lineWidth : Float = SizeUtils.dp2px(1f)

    private val codeTextSize: Float = SizeUtils.sp2px(16f)//验证码文字大小

    private var offsetBorder:Float = SizeUtils.dp2px(10f)//边框偏移量

    private var paddingBorder:Float = SizeUtils.dp2px(10f)//边框内间距

    private var widthCode : Float = 7 * codeTextSize//验证码的宽度

    private val heightCode:Float = codeTextSize


        init {
            //验证码
            codePaint.color = colorCode
            codePaint.textSize = SizeUtils.sp2px(16f)
            codePaint.style = Paint.Style.FILL//填充内部

            //干扰线
            linePaint.color = colorLine
            linePaint.strokeWidth = lineWidth
            linePaint.style = Paint.Style.STROKE//填充内部

            //边框
            borderPaint.color =colorBorder
            borderPaint.style = Paint.Style.STROKE//填充内部
            borderPaint.strokeWidth = lineWidth

        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(setMeasureWidth(widthMeasureSpec),
            setMeasureHeight(heightMeasureSpec))
    }


    private fun setMeasureHeight(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = (heightCode + (offsetBorder*2) + lineWidth + paddingBorder).toInt()
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    private fun setMeasureWidth(measureSpec: Int): Int {
        var result: Int
        val mode : Int = MeasureSpec.getMode(measureSpec)
        val size: Int = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = (widthCode + (offsetBorder*2) + lineWidth + paddingBorder).toInt() //根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)//是否需要边框？
        if (TextUtils.isEmpty(checkCodeText)){
            //绘制没有验证码的提示
            drawCodeNot(canvas)
        }else{
            //绘制验证码文本
            drawCodeText(canvas)
            //绘制干扰线
            drawLines(canvas)
        }



    }

    private fun drawCodeNot(canvas: Canvas) {
        val str = "获取验证码!"
        val codeX = offsetBorder+paddingBorder
        val codeY = offsetBorder+paddingBorder + heightCode/2f
        codePaint.color = Color.BLACK
        canvas.drawText(str,codeX ,codeY, codePaint)
    }


    //绘制干扰线
    private fun drawLines(canvas: Canvas) {
        canvas.drawLine(((width-widthCode)/2f),height/2f, (width-widthCode)/2f+ widthCode,height/2f,linePaint)
    }


    /**绘制验证码文本*/
    private fun drawCodeText(canvas: Canvas) {

        var array:CharArray = checkCodeText.toCharArray()

        var widthStr: Float = widthCode/array.size

        for ((index,e) in array.withIndex() ){
            val randoms = (1..27).random()
            val color = (0xFF000001..0xFFFFFFFE).random()
            codePaint.color = color.toInt()
            val codeX = (widthStr*index)+((width-widthCode)/2f)
            val codeY = offsetBorder+paddingBorder + heightCode/2f
            canvas.rotate(randoms*10f,codeX + (widthStr/2) ,height/2f )//旋转
            canvas.drawText(e.toString(),codeX ,codeY, codePaint)
            canvas.rotate(-(randoms*10f),codeX + (widthStr/2) ,height/2f )//还原
        }
    }

    /**设置验证码*/
     fun setCode(str :String){
        checkCodeText = str
        widthCode  = checkCodeText.length * codeTextSize

        invalidate()
    }




    /**绘制边框*/
    private fun drawBorder(canvas: Canvas) {
        /**绘制边框*/
    canvas.drawRect(offsetBorder,offsetBorder, width-offsetBorder, height-offsetBorder,borderPaint)

    }


}