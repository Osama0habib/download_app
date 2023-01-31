package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress = 0
    private var widthSize = 0
    private var heightSize = 0
    private val valueAnimator = ValueAnimator.ofInt(0,360).setDuration(1000).apply {
        addUpdateListener {
           progress = it.animatedValue as Int
            invalidate()
        }


        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART
    }


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Idle) { p, old, new ->
            println(old)
            println(new)
        when (new){
            ButtonState.Loading -> {
                valueAnimator.start()
                invalidate()
            }
            ButtonState.Idle -> {
                valueAnimator.cancel()
                invalidate()
            }
        }
    }
    private val rectInset = resources.getDimension(R.dimen.fab_margin)
    private val fontSize = resources.getDimension(R.dimen.default_text_size)



    init {
        isClickable = true
    }

//        override fun performClick(): Boolean {
//        if (super.performClick()) return true
//            println("performClick")
//
//        buttonState = ButtonState.Loading
//        invalidate()
//        return true
//    }
    override fun performClick(): Boolean {
        buttonState = ButtonState.Loading
        invalidate()
    super.performClick()
    return true;

    }

    fun downloadComplete(){
        buttonState = ButtonState.Idle
        progress = 0
        invalidate()

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.CYAN
        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(),25f,25f, paint)
        canvas?.save()

        paint.color = Color.GREEN
        canvas?.drawRoundRect(0f , 0f,
            ((width * progress) / 360).toFloat(), height.toFloat(),25f,25f, paint)
        canvas?.save()


        paint.color = Color.WHITE
// Align the RIGHT side of the text with the origin.
        paint.textSize = fontSize
        paint.textAlign = Paint.Align.CENTER

//        canvas?.drawArc(700f,(height/2).toFloat() -35f,630f,(height/2).toFloat() + 35f,0f,
//            progress.toFloat(),true,paint)
        canvas?.drawArc(700f,(height/2).toFloat() -35f,630f,(height/2).toFloat() + 35f,0f,
            progress.toFloat(),true,paint)
        canvas?.save()
        val xPos = ( canvas!!.width /2f).toInt()
        val yPos = (canvas!!.height / 2f - ((paint.descent() + paint.ascent()) / 2)).toInt()

        canvas.drawText("Download", xPos.toFloat(), yPos.toFloat(), paint)
        canvas.save()



        canvas.restore()

    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}