package com.bernacchi.mx5manual

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat

class LetterScreen(carContext: CarContext, private val letter: Char) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        val entries = ManualRepository.byLetter(letter)

        val listBuilder = ItemList.Builder()
        for (entry in entries) {
            val rowBuilder = Row.Builder()
                .setTitle(entry.label)
                .addText("pag. " + entry.pages.joinToString(", "))
                .setOnClickListener {
                    screenManager.push(DetailScreen(carContext, entry.id))
                }

            if (entry.label.contains("spia", ignoreCase = true) ||
                entry.label.contains("indicatore luminoso", ignoreCase = true)
            ) {
                rowBuilder.setImage(
                    CarIcon.Builder(
                        IconCompat.createWithResource(carContext, R.drawable.ic_warning)
                    ).build()
                )
            }

            listBuilder.addItem(rowBuilder.build())
        }

        return ListTemplate.Builder()
            .setTitle(letter.toString())
            .setHeaderAction(Action.BACK)
            .setSingleList(listBuilder.build())
            .build()
    }
}
