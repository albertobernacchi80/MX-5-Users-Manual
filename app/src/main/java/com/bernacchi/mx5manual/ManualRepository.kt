package com.bernacchi.mx5manual

import android.content.Context
import org.json.JSONArray

data class ManualEntry(
    val id: String,
    val label: String,
    val pages: List<String>,
    val text: String
)

object ManualRepository {

    private var entries: List<ManualEntry> = emptyList()
    private var loaded = false

    fun ensureLoaded(context: Context) {
        if (loaded) return
        val json = context.assets.open("manual_index.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val array = JSONArray(json)
        val list = ArrayList<ManualEntry>(array.length())
        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            val pagesArr = o.getJSONArray("pages")
            val pages = ArrayList<String>(pagesArr.length())
            for (j in 0 until pagesArr.length()) pages.add(pagesArr.getString(j))
            list.add(
                ManualEntry(
                    id = o.getString("id"),
                    label = o.getString("label"),
                    pages = pages,
                    text = o.getString("text")
                )
            )
        }
        entries = list.sortedBy { normalizeForSort(it.label) }
        loaded = true
    }

    fun all(): List<ManualEntry> = entries

    fun letters(): List<Char> =
        entries.map { firstLetter(it.label) }.distinct().sorted()

    fun byLetter(letter: Char): List<ManualEntry> =
        entries.filter { firstLetter(it.label) == letter }

    fun byId(id: String): ManualEntry? = entries.firstOrNull { it.id == id }

    fun search(query: String): List<ManualEntry> {
        if (query.isBlank()) return emptyList()
        val q = normalizeForSort(query)
        return entries.filter { normalizeForSort(it.label).contains(q) }
    }

    private fun firstLetter(label: String): Char {
        val c = normalizeForSort(label).firstOrNull { it.isLetter() } ?: '#'
        return c.uppercaseChar()
    }

    private fun normalizeForSort(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replace(Regex("\\p{M}"), "")
            .lowercase()
}
