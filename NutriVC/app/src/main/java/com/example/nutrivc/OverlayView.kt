package com.example.nutrivc


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var bounds = Rect()
    private var cacheBitmap: Bitmap? = null
    private var cacheCanvas: Canvas? = null

    init {
        initPaints()
    }

    fun clear() {
        results = listOf()
        cacheBitmap = null
        invalidate()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.BLACK
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        cacheCanvas = Canvas(cacheBitmap!!)
    }
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        Log.d("OverlayView", "Desenhando OverlayView")

        if (results.isNotEmpty()) {
            cacheCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            results.forEach {
                val left = it.x1 * width
                val top = it.y1 * height
                val right = it.x2 * width
                val bottom = it.y2 * height

                cacheCanvas?.drawRect(left, top, right, bottom, boxPaint)
                val drawableText = it.clsName

                textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
                val textWidth = bounds.width()
                val textHeight = bounds.height()
                cacheCanvas?.drawRect(
                    left,
                    top,
                    left + textWidth + BOUNDING_RECT_TEXT_PADDING,
                    top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )
                cacheCanvas?.drawText(drawableText, left, top + bounds.height(), textPaint)
            }
        }
        canvas.drawBitmap(cacheBitmap!!, 0f, 0f, null)
    }

    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
        Log.d("OverlayView", "Resultados atualizados: $results")
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}