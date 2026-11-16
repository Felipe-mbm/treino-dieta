package com.felipe.treinodieta.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/** DataStore próprio para as metas (arquivo "metas.preferences_pb"). */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "metas")

/** Metas diárias de kcal e macros. Valores padrão razoáveis para musculação. */
data class Metas(
    val kcal: Double = 2000.0,
    val proteinaG: Double = 130.0,
    val carboidratoG: Double = 220.0,
    val gorduraG: Double = 60.0,
)

/**
 * Metas diárias guardadas em DataStore Preferences — valores simples e únicos
 * não precisam de tabela no Room.
 */
class MetaRepository(private val context: Context) {

    private object Chaves {
        val kcal = doublePreferencesKey("meta_kcal")
        val proteina = doublePreferencesKey("meta_proteina_g")
        val carboidrato = doublePreferencesKey("meta_carboidrato_g")
        val gordura = doublePreferencesKey("meta_gordura_g")
    }

    val metas: Flow<Metas> = context.dataStore.data
        .catch { emit(emptyPreferences()) } // leitura nunca derruba o app
        .map { prefs ->
            Metas(
                kcal = prefs[Chaves.kcal] ?: 2000.0,
                proteinaG = prefs[Chaves.proteina] ?: 130.0,
                carboidratoG = prefs[Chaves.carboidrato] ?: 220.0,
                gorduraG = prefs[Chaves.gordura] ?: 60.0,
            )
        }

    suspend fun salvar(metas: Metas) {
        context.dataStore.edit { prefs ->
            prefs[Chaves.kcal] = metas.kcal
            prefs[Chaves.proteina] = metas.proteinaG
            prefs[Chaves.carboidrato] = metas.carboidratoG
            prefs[Chaves.gordura] = metas.gorduraG
        }
    }
}
