package com.bernacchi.mx5manual

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.Row
import androidx.car.app.model.SearchTemplate
import androidx.car.app.model.Template

class SearchScreen(carContext: CarContext) : Screen(carContext) {

    private var results: ItemList = emptyResultList("Digita per cercare…")

    override fun onGetTemplate(): Template {
        val callback = object : SearchTemplate.SearchCallback {
            override fun onSearchTextChanged(searchText: String) {
                updateResults(searchText)
            }

            override fun onSearchSubmitted(searchText: String) {
                updateResults(searchText)
            }
        }

        return SearchTemplate.Builder(callback)
            .setHeaderAction(Action.BACK)
            .setShowKeyboardByDefault(true)
            .setSearchHint("Cerca nel manuale…")
            .setItemList(results)
            .build()
    }

    private fun updateResults(query: String) {
        if (query.isBlank()) {
            results = emptyResultList("Digita per cercare…")
            invalidate()
            return
        }
        val matches = ManualRepository.search(query).take(50)
        if (matches.isEmpty()) {
            results = emptyResultList("Nessun risultato")
            invalidate()
            return
        }
        val builder = ItemList.Builder()
        for (m in matches) {
            builder.addItem(
                Row.Builder()
                    .setTitle(m.label)
                    .addText("pag. " + m.pages.joinToString(", "))
                    .setOnClickListener {
                        screenManager.push(DetailScreen(carContext, m.id))
                    }
                    .build()
            )
        }
        results = builder.build()
        invalidate()
    }

    private fun emptyResultList(message: String): ItemList =
        ItemList.Builder().setNoItemsMessage(message).build()
}
