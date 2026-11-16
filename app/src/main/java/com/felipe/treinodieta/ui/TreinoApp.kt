package com.felipe.treinodieta.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.felipe.treinodieta.R
import com.felipe.treinodieta.ui.screens.DietaScreen
import com.felipe.treinodieta.ui.screens.HojeScreen
import com.felipe.treinodieta.ui.screens.ProgressoScreen
import com.felipe.treinodieta.ui.screens.TreinosScreen

private data class Aba(
    val rota: String,
    @StringRes val rotuloRes: Int,
    val icone: ImageVector,
)

private val abas = listOf(
    Aba(rota = "hoje", rotuloRes = R.string.aba_hoje, icone = Icons.Filled.Home),
    Aba(rota = "treinos", rotuloRes = R.string.aba_treinos, icone = Icons.Filled.FitnessCenter),
    Aba(rota = "dieta", rotuloRes = R.string.aba_dieta, icone = Icons.Filled.Restaurant),
    Aba(rota = "progresso", rotuloRes = R.string.aba_progresso, icone = Icons.AutoMirrored.Filled.ShowChart),
)

/**
 * Estrutura principal: barra inferior com 4 abas + NavHost.
 * Nas próximas fases, telas secundárias (sessão de treino, biblioteca de
 * alimentos, metas) entram aqui como rotas extras, ex.: "sessao/{id}".
 */
@Composable
fun TreinoApp() {
    val navController = rememberNavController()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                abas.forEach { aba ->
                    NavigationBarItem(
                        selected = rotaAtual == aba.rota,
                        onClick = {
                            // Padrão de navegação entre abas: volta à raiz e
                            // preserva o estado de cada aba
                            navController.navigate(aba.rota) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(aba.icone, contentDescription = stringResource(aba.rotuloRes))
                        },
                        label = { Text(stringResource(aba.rotuloRes)) },
                    )
                }
            }
        },
    ) { paddingInterno ->
        NavHost(
            navController = navController,
            startDestination = "hoje",
            modifier = Modifier.padding(paddingInterno),
        ) {
            composable("hoje") { HojeScreen() }
            composable("treinos") { TreinosScreen() }
            composable("dieta") { DietaScreen() }
            composable("progresso") { ProgressoScreen() }
        }
    }
}
