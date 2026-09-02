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
 * Registra la Surface di disegno libero che l'host mette a disposizione alle schermate
 * NavigationTemplate, e la ridisegna quando serve. Riusato da più schermate (splash,
 * dettaglio voce): ognuna fornisce il proprio DrawCallback e richiama renderNow() quando
 * il proprio stato cambia (es. cambio pagina), dato che la Surface non si ridisegna da sola.
 */
class CanvasSurfaceRenderer(private val carContext: CarContext) {

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
            this@CanvasSurfaceRenderer.visibleArea = visibleArea
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

    /** Richiede un nuovo disegno con lo stato corrente (es. dopo un cambio pagina). */
    fun renderNow() = render()

    fun currentVisibleArea(): Rect? = visibleArea

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
