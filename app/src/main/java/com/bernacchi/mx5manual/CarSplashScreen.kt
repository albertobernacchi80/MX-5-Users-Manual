package com.bernacchi.mx5manual

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Schermata di avvio a schermo intero sul display dell'auto: silhouette reale della MX-5
 * e titolo ("MX-5" in rosso, il resto in bianco) disegnati su Canvas tramite la Surface
 * esposta da NavigationTemplate — stessa tecnica di Mx-5 Driver's Metrics. Passa da sola
 * alla Home dopo una breve pausa; l'ActionStrip offre "Salta" per chi non vuole aspettare.
 */
class CarSplashScreen(carContext: CarContext) : Screen(carContext), DefaultLifecycleObserver {

    companion object {
        private val RED = Color.parseColor("#E24B4A")
        private val WHITE = Color.parseColor("#F4F4FA")
        private const val DELAY_MS = 1600L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val renderer = CanvasSurfaceRenderer(carContext)
    private var silhouette: Bitmap? = null
    private var navigated = false
    private val goHome = Runnable { goToHome() }

    init {
        lifecycle.addObserver(this)
    }

    private fun goToHome() {
        if (navigated) return
        navigated = true
        screenManager.push(HomeScreen(carContext))
        finish()
    }

    override fun onGetTemplate(): Template {
        val actionStrip = ActionStrip.Builder()
            .addAction(
                Action.Builder()
                    .setTitle("Salta")
                    .setOnClickListener { goToHome() }
                    .build()
            )
            .build()

        return NavigationTemplate.Builder()
            .setActionStrip(actionStrip)
            .build()
    }

    private fun draw(canvas: Canvas, visibleArea: Rect) {
        if (silhouette == null) {
            silhouette = BitmapFactory.decodeResource(carContext.resources, R.drawable.car_silhouette)
        }
        val bmp = silhouette ?: return

        val cx = visibleArea.centerX().toFloat()
        val cy = visibleArea.centerY().toFloat()
        val areaW = visibleArea.width().toFloat()
        val areaH = visibleArea.height().toFloat()

        val maxW = areaW * 0.78f
        val maxH = areaH * 0.42f
        val scale = minOf(maxW / bmp.width, maxH / bmp.height)
        val w = bmp.width * scale
        val h = bmp.height * scale
        val offsetY = areaH * 0.06f
        val dst = RectF(cx - w / 2f, cy - h / 2f - offsetY, cx + w / 2f, cy + h / 2f - offsetY)
        val imgPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bmp, null, dst, imgPaint)

        val mx5 = "MX-5"
        val rest = " Manuale utente"
        val titleY = cy + areaH * 0.22f
        val titleSize = minOf(areaW, areaH) * 0.075f

        val mx5Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = RED
            isFakeBoldText = true
            textSize = titleSize
        }
        val restPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            isFakeBoldText = true
            textSize = titleSize
        }

        val mx5W = mx5Paint.measureText(mx5)
        val restW = restPaint.measureText(rest)
        val startX = cx - (mx5W + restW) / 2f

        canvas.drawText(mx5, startX, titleY, mx5Paint)
        canvas.drawText(rest, startX + mx5W, titleY, restPaint)
    }

    override fun onStart(owner: LifecycleOwner) {
        renderer.start { canvas, area -> draw(canvas, area) }
        handler.postDelayed(goHome, DELAY_MS)
    }

    override fun onStop(owner: LifecycleOwner) {
        handler.removeCallbacks(goHome)
    }
}
