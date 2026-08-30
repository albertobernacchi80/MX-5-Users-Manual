package com.bernacchi.mx5manual

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

class DetailScreen(carContext: CarContext, private val entryId: String) : Screen(carContext) {

    companion object {
        private const val MAX_ROWS = 70
        private const val LINE_LEN = 90
    }

    override fun onGetTemplate(): Template {
        val entry = ManualRepository.byId(entryId)
        val listBuilder = ItemList.Builder()

        if (entry == null) {
            listBuilder.addItem(Row.Builder().setTitle("Voce non trovata").build())
            return ListTemplate.Builder()
                .setTitle("Errore")
                .setHeaderAction(Action.BACK)
                .setSingleList(listBuilder.build())
                .build()
        }

        val lines = wrapText(entry.text, LINE_LEN)
        var truncated = false
        for ((i, line) in lines.withIndex()) {
            if (i >= MAX_ROWS) {
                truncated = true
                break
            }
            listBuilder.addItem(Row.Builder().setTitle(line).build())
        }
        if (truncated) {
            listBuilder.addItem(
                Row.Builder()
                    .setTitle("[Testo troncato — vedi manuale cartaceo, pag. ${entry.pages.joinToString(", ")}]")
                    .build()
            )
        }

        return ListTemplate.Builder()
            .setTitle(entry.label)
            .setHeaderAction(Action.BACK)
            .setSingleList(listBuilder.build())
            .build()
    }

    /** Spezza il testo in righe brevi leggibili, rispettando le interruzioni di paragrafo. */
    private fun wrapText(text: String, maxLen: Int): List<String> {
        val result = ArrayList<String>()
        for (paragraph in text.split("\n")) {
            val trimmed = paragraph.trim()
            if (trimmed.isEmpty()) continue
            var current = StringBuilder()
            for (word in trimmed.split(" ")) {
                if (word.isBlank()) continue
                if (current.isEmpty()) {
                    current.append(word)
                } else if (current.length + 1 + word.length <= maxLen) {
                    current.append(' ').append(word)
                } else {
                    result.add(current.toString())
                    current = StringBuilder(word)
                }
            }
            if (current.isNotEmpty()) result.add(current.toString())
        }
        return result
    }
}
