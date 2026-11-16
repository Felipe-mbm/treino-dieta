package com.felipe.treinodieta.di

import android.content.Context
import com.felipe.treinodieta.data.local.AppDatabase
import com.felipe.treinodieta.data.repository.DietaRepository
import com.felipe.treinodieta.data.repository.TreinoRepository
import com.felipe.treinodieta.data.settings.MetaRepository

/**
 * Container de injeção de dependência manual: um único ponto que cria e
 * conecta banco, repositórios e DataStore. Simples e explícito — ideal para
 * o MVP. Alternativa futura: migrar para Hilt quando o app crescer.
 */
class AppContainer(context: Context) {

    private val database = AppDatabase.criar(context)

    val treinoRepository = TreinoRepository(
        sessaoDao = database.sessaoDao(),
        exercicioDao = database.exercicioDao(),
        registroDao = database.registroDao(),
    )

    val dietaRepository = DietaRepository(
        refeicaoDao = database.refeicaoDao(),
        alimentoDao = database.alimentoDao(),
    )

    val metaRepository = MetaRepository(context)
}
