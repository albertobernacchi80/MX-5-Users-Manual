package com.bernacchi.mx5manual

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: android.widget.TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ManualRepository.ensureLoaded(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        emptyText = findViewById(R.id.emptyText)

        val searchField = findViewById<EditText>(R.id.searchField)
        searchField.requestFocus()
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateResults(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        updateResults("")
    }

    private fun updateResults(query: String) {
        if (query.isBlank()) {
            recyclerView.adapter = SimpleRowAdapter(emptyList())
            emptyText.visibility = android.view.View.VISIBLE
            emptyText.text = "Digita per cercare…"
            return
        }
        val results = ManualRepository.search(query)
        if (results.isEmpty()) {
            recyclerView.adapter = SimpleRowAdapter(emptyList())
            emptyText.visibility = android.view.View.VISIBLE
            emptyText.text = "Nessun risultato"
            return
        }
        emptyText.visibility = android.view.View.GONE
        val rows = results.map { entry ->
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
