package com.bernacchi.mx5manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "entry_id"

        fun intentFor(context: Context, entryId: String): Intent =
            Intent(context, DetailActivity::class.java).putExtra(EXTRA_ID, entryId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ManualRepository.ensureLoaded(this)

        val entryId = intent.getStringExtra(EXTRA_ID)
        val entry = entryId?.let { ManualRepository.byId(it) }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = entry?.label ?: "Voce non trovata"
        toolbar.setTitleTextColor(0xFFF2F2F3.toInt())
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener { finish() }

        val pagesLabel = findViewById<TextView>(R.id.pagesLabel)
        val bodyText = findViewById<TextView>(R.id.bodyText)

        if (entry == null) {
            pagesLabel.text = ""
            bodyText.text = "Voce non trovata."
        } else {
            pagesLabel.text = "pag. " + entry.pages.joinToString(", ")
            bodyText.text = entry.text
        }
    }
}
