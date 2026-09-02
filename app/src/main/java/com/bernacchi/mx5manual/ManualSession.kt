package com.bernacchi.mx5manual

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session

class ManualSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen {
        ManualRepository.ensureLoaded(carContext)
        return CarSplashScreen(carContext)
    }
}
