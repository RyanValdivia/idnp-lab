package com.example.planointeractivo_compose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import com.example.planointeractivo_compose.Variante

// Typography base con la familia tipografica de la variante aplicada a todos los estilos.
private fun TextStyle.conFuente() = copy(fontFamily = Variante.fuente)

val Typography = Typography().let { t ->
    t.copy(
        displayLarge = t.displayLarge.conFuente(),
        displayMedium = t.displayMedium.conFuente(),
        displaySmall = t.displaySmall.conFuente(),
        headlineLarge = t.headlineLarge.conFuente(),
        headlineMedium = t.headlineMedium.conFuente(),
        headlineSmall = t.headlineSmall.conFuente(),
        titleLarge = t.titleLarge.conFuente(),
        titleMedium = t.titleMedium.conFuente(),
        titleSmall = t.titleSmall.conFuente(),
        bodyLarge = t.bodyLarge.conFuente(),
        bodyMedium = t.bodyMedium.conFuente(),
        bodySmall = t.bodySmall.conFuente(),
        labelLarge = t.labelLarge.conFuente(),
        labelMedium = t.labelMedium.conFuente(),
        labelSmall = t.labelSmall.conFuente()
    )
}
