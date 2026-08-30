package com.bernacchi.mx5manual

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.Surface
import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer

/**
 * Registra la Surface di disegno personalizzato che l'host mette a disposizione alle
 * schermate NavigationTemplate. A differenza dei gauge di un cruscotto, qui il contenuto
 * (silhouette + titolo) è statico: si ridisegna solo quando la Surface diventa disponibile
 * o cambia l'area visibile, senza bisogno di un tick periodico.
 */
class SplashSurfaceRenderer(private val carContext: CarContext) {

    fun interface DrawCallback {
        fun draw(canvas: Canvas, visibleArea: Rect)
    }

    private var surface: Surface? = null
    private var visibleArea: Rect? = null
    private var drawCallback: DrawCallback? = null

    private val surfaceCallback = object : SurfaceCallback {
        override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
            surface = surfaceContainer.surface
            render()
        }

        override fun onVisibleAreaChanged(visibleArea: Rect) {
            this@SplashSurfaceRenderer.visibleArea = visibleArea
            render()
        }

        override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
            surface = null
        }
    }

    fun start(callback: DrawCallback) {
        drawCallback = callback
        carContext.getCarService(AppManager::class.java).setSurfaceCallback(surfaceCallback)
        render()
    }

    private fun render() {
        val s = surface
        val cb = drawCallback
        if (s == null || !s.isValid || cb == null) return
        val canvas = try {
            s.lockCanvas(null)
        } catch (e: Exception) {
            return
        }
        try {
            canvas.drawColor(Color.parseColor("#0B0B0D"))
            val area = visibleArea ?: Rect(0, 0, canvas.width, canvas.height)
            cb.draw(canvas, area)
        } finally {
            s.unlockCanvasAndPost(canvas)
        }
    }
}
