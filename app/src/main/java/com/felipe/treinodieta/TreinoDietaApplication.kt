package com.felipe.treinodieta

import android.app.Application
import com.felipe.treinodieta.di.AppContainer

/**
 * Ponto de criação das dependências do app (injeção manual — ver [AppContainer]).
 * Uma única Activity consome tudo via Compose.
 */
class TreinoDietaApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
