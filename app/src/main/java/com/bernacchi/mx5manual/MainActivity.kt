package com.bernacchi.mx5manual

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Home dell'app sul telefono: stessi 9 argomenti di manutenzione curati mostrati
 * su Android Auto (vedi HomeScreen), più accesso a indice A-Z e ricerca dalla toolbar.
 */
class MainActivity : AppCompatActivity() {

    companion object {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        ManualRepository.ensureLoaded(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Manutenzione"
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val rows = TOPIC_IDS.mapNotNull { id ->
            ManualRepository.byId(id)?.let { entry ->
                SimpleRowAdapter.RowData(
                    title = entry.label,
                    subtitle = "pag. " + entry.pages.joinToString(", ")
                ) {
                    startActivity(DetailActivity.intentFor(this, entry.id))
                }
            }
        }
        recyclerView.adapter = SimpleRowAdapter(rows)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.action_index -> {
                startActivity(Intent(this, AlphabetIndexActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
