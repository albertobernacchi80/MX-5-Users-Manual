package com.bernacchi.mx5manual

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlphabetIndexActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        ManualRepository.ensureLoaded(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Indice A-Z"
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val rows = ManualRepository.letters().map { letter ->
            val count = ManualRepository.byLetter(letter).size
            SimpleRowAdapter.RowData(
                title = letter.toString(),
                subtitle = "$count voci"
            ) {
                startActivity(LetterActivity.intentFor(this, letter))
            }
        }
        recyclerView.adapter = SimpleRowAdapter(rows)
    }
}
