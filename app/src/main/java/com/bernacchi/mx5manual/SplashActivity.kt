package com.bernacchi.mx5manual

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Schermata mostrata quando si apre l'app dal telefono. Dopo una breve pausa passa
 * alla Home vera e propria (MainActivity, interfaccia touch normale) — l'app è quindi
 * utilizzabile sia da telefono sia, separatamente, tramite Android Auto (Home/Lettera/
 * Dettaglio/Ricerca gestiti dalla Car App Library, codice indipendente da questo).
 */
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DURATION_MS = 900L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DURATION_MS)
    }
}
