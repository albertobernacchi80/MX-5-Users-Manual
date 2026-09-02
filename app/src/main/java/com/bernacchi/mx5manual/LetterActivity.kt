package com.bernacchi.mx5manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LetterActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_LETTER = "letter"

        fun intentFor(context: Context, letter: Char): Intent =
            Intent(context, LetterActivity::class.java).putExtra(EXTRA_LETTER, letter.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        ManualRepository.ensureLoaded(this)

        val letter = intent.getStringExtra(EXTRA_LETTER)?.firstOrNull() ?: '#'

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = letter.toString()
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val rows = ManualRepository.byLetter(letter).map { entry ->
            SimpleRowAdapter.RowData(
                title = entry.label,
                subtitle = "pag. " + entry.pages.joinToString(", ")
            ) {
                startActivity(DetailActivity.intentFor(this, entry.id))
            }
        }
        recyclerView.adapter = SimpleRowAdapter(rows)
    }
}
