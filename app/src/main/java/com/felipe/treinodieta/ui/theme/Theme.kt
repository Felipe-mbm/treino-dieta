package com.felipe.treinodieta.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val EsquemaEscuro = darkColorScheme(
    primary = RoxoClaro,
    secondary = RoxoCinzaClaro,
    tertiary = RosaClaro,
)

private val EsquemaClaro = lightColorScheme(
    primary = RoxoEscuro,
    secondary = RoxoCinzaEscuro,
    tertiary = RosaEscuro,
)

@Composable
fun TreinoDietaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Cores dinâmicas (Material You) no Android 12+; usa o papel de parede
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> EsquemaEscuro
        else -> EsquemaClaro
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
