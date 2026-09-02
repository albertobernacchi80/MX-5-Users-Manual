package com.bernacchi.mx5manual

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Testo della pagina del manuale disegnato su Canvas (non una ListTemplate): con le righe
 * di lista ogni riga è comunque un punto di selezione per la rotellina anche senza azione
 * collegata, dando l'impressione errata che tutto sia "cliccabile". Qui il testo è puro
 * disegno, paginato con l'azione "Pagina succ." — l'ActionStrip di NavigationTemplate su
 * questo head unit rende raggiungibili con la rotellina solo le prime 2 azioni anche se
 * ne disegna di più, quindi ci si ferma a 2: Indietro e Pagina succ. (che dopo l'ultima
 * torna alla prima, così tutte le pagine restano raggiungibili).
 */
class DetailScreen(carContext: CarContext, private val entryId: String) :
    Screen(carContext), DefaultLifecycleObserver {

    companion object {
        private val WHITE = Color.parseColor("#F4F4FA")
        private val MUTED = Color.parseColor("#8888A0")
        private const val LINE_SIZE_SP = 15f
        private const val TITLE_SIZE_SP = 19f
        private const val LINE_SPACING = 1.5f
    }

    private val renderer = CanvasSurfaceRenderer(carContext)
    private val entry = ManualRepository.byId(entryId)

    private var wrappedLines: List<String>? = null
    private var currentPage = 0
    private var totalPages = 1

    init {
        lifecycle.addObserver(this)
    }

    override fun onGetTemplate(): Template {
        val actionStrip = ActionStrip.Builder()
            .addAction(
                Action.Builder()
                    .setTitle("Indietro")
                    .setOnClickListener { screenManager.pop() }
                    .build()
            )
            .addAction(
                Action.Builder()
                    .setTitle("Pagina succ. ▶")
                    .setOnClickListener {
                        if (totalPages > 1) {
                            currentPage = (currentPage + 1) % totalPages
                            renderer.renderNow()
                        }
                    }
                    .build()
            )
            .build()

        return NavigationTemplate.Builder()
            .setActionStrip(actionStrip)
            .build()
    }

    private fun draw(canvas: Canvas, visibleArea: Rect) {
        val density = carContext.resources.displayMetrics.density
        val lineSize = LINE_SIZE_SP * density
        val titleSize = TITLE_SIZE_SP * density
        val marginX = visibleArea.left + 28f * density
        val maxWidth = visibleArea.width() - 56f * density

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            isFakeBoldText = true
            textSize = titleSize
        }
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            textSize = lineSize
        }
        val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = MUTED
            textSize = lineSize * 0.8f
        }

        if (entry == null) {
            canvas.drawText("Voce non trovata", marginX, visibleArea.top + titleSize * 2, titlePaint)
            return
        }

        if (wrappedLines == null) {
            wrappedLines = wrapText(entry.text, linePaint, maxWidth)
        }
        val lines = wrappedLines ?: emptyList()

        val titleY = visibleArea.top + titleSize + 24f * density
        val bodyTop = titleY + 24f * density
        val bodyBottom = visibleArea.bottom - 40f * density
        val lineHeight = lineSize * LINE_SPACING
        val linesPerPage = ((bodyBottom - bodyTop) / lineHeight).toInt().coerceAtLeast(1)
        totalPages = ((lines.size + linesPerPage - 1) / linesPerPage).coerceAtLeast(1)
        if (currentPage >= totalPages) currentPage = 0

        // Titolo (troncato se troppo lungo per una riga)
        val title = ellipsize(entry.label, titlePaint, maxWidth)
        canvas.drawText(title, marginX, titleY, titlePaint)

        val start = currentPage * linesPerPage
        val end = (start + linesPerPage).coerceAtMost(lines.size)
        var y = bodyTop
        for (i in start until end) {
            canvas.drawText(lines[i], marginX, y, linePaint)
            y += lineHeight
        }

        if (totalPages > 1) {
            val footer = "Pagina ${currentPage + 1} di $totalPages"
            canvas.drawText(footer, marginX, visibleArea.bottom - 12f * density, footerPaint)
        }
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val result = ArrayList<String>()
        for (paragraph in text.split("\n")) {
            val trimmed = paragraph.trim()
            if (trimmed.isEmpty()) continue
            var current = StringBuilder()
            for (word in trimmed.split(" ")) {
                if (word.isBlank()) continue
                val trial = if (current.isEmpty()) word else "${current} $word"
                if (paint.measureText(trial) <= maxWidth) {
                    current = StringBuilder(trial)
                } else {
                    if (current.isNotEmpty()) result.add(current.toString())
                    current = StringBuilder(word)
                }
            }
            if (current.isNotEmpty()) result.add(current.toString())
        }
        return result
    }

    private fun ellipsize(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var t = text
        while (t.isNotEmpty() && paint.measureText("$t…") > maxWidth) {
            t = t.dropLast(1)
        }
        return "$t…"
    }

    override fun onStart(owner: LifecycleOwner) {
        renderer.start { canvas, area -> draw(canvas, area) }
    }
}
