package com.bernacchi.mx5manual

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

class MainCarAppService : CarAppService() {

    override fun createHostValidator(): HostValidator {
        // App per uso personale, non distribuita: accetta qualunque host valido (es. Android Auto).
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun onCreateSession(): Session {
        return ManualSession()
    }
}
