package com.bernacchi.mx5manual

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat

/**
 * Home dell'app: argomenti di manutenzione più consultati in caso di necessità
 * (livelli, pneumatici, fusibili, batteria scarica), scelti tra le voci del manuale.
 * L'indice alfabetico completo resta disponibile tramite l'azione "A-Z".
 */
class HomeScreen(carContext: CarContext) : Screen(carContext) {

    companion object {
        // id delle voci curate, presi da manual_index.json (rigenerato: indice riparso senza
        // gerarchia inventata, testo integrale per tutte le voci)
        private val TOPIC_IDS = listOf(
            "e0207", // Programma di manutenzione
            "e0043", // Controllo livello olio motore
            "e0044", // Controllo livello refrigerante
            "e0041", // Controllo livello fluido freni/frizione
            "e0042", // Controllo livello fluido lavavetri
            "e0239", // Pressione di gonfiaggio pneumatici
            "e0242", // Sostituzione di uno pneumatico
            "e0109", // Fusibili
            "e0005"  // Batteria scarica - avviamento con mezzo di soccorso
        )
    }

    override fun onGetTemplate(): Template {
        val listBuilder = ItemList.Builder()
        for (id in TOPIC_IDS) {
            val entry = ManualRepository.byId(id) ?: continue
            listBuilder.addItem(
                Row.Builder()
                    .setTitle(entry.label)
                    .addText("pag. " + entry.pages.joinToString(", "))
                    .setOnClickListener {
                        screenManager.push(DetailScreen(carContext, entry.id))
                    }
                    .build()
            )
        }

        val searchAction = Action.Builder()
            .setIcon(
                CarIcon.Builder(
                    IconCompat.createWithResource(carContext, android.R.drawable.ic_menu_search)
                ).build()
            )
            .setOnClickListener {
                screenManager.push(SearchScreen(carContext))
            }
            .build()

        val indexAction = Action.Builder()
            .setTitle("A-Z")
            .setOnClickListener {
                screenManager.push(AlphabetIndexScreen(carContext))
            }
            .build()

        return ListTemplate.Builder()
            .setTitle("Manutenzione")
            .setHeaderAction(Action.APP_ICON)
            .setActionStrip(
                ActionStrip.Builder()
                    .addAction(indexAction)
                    .addAction(searchAction)
                    .build()
            )
            .setSingleList(listBuilder.build())
            .build()
    }
}
