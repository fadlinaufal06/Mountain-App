package com.bangkit.mountainapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.bangkit.mountainapp.R

class MyCanvasAuth : View {
    private val mPaint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.color = ResourcesCompat.getColor(resources, R.color.pale_cornflower_blue, null)
        val left = -20F
        val top = -20F - this.height.toFloat() / 2
        val right = this.width.toFloat() + 20F
        val bottom = this.height.toFloat()
        canvas.drawOval(left, top, right, bottom, mPaint)
    }
}