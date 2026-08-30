package com.bernacchi.mx5manual

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Schermata mostrata quando si apre l'app dal telefono. Non c'è nessun'altra
 * schermata verso cui passare: le schermate vere e proprie (Home, Lettera,
 * Dettaglio, Ricerca) sono gestite dalla Car App Library ed esistono solo
 * quando il telefono proietta su Android Auto, non come Activity sul telefono.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
