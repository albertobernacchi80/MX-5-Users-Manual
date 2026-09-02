package com.bernacchi.mx5manual

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template

class AlphabetIndexScreen(carContext: CarContext) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        val letters = ManualRepository.letters()

        val listBuilder = ItemList.Builder()
        for (letter in letters) {
            val count = ManualRepository.byLetter(letter).size
            listBuilder.addItem(
                Row.Builder()
                    .setTitle(letter.toString())
                    .addText("$count voci")
                    .setOnClickListener {
                        screenManager.push(LetterScreen(carContext, letter))
                    }
                    .build()
            )
        }

        return ListTemplate.Builder()
            .setTitle("Indice A-Z")
            .setHeaderAction(Action.BACK)
            .setSingleList(listBuilder.build())
            .build()
    }
}
