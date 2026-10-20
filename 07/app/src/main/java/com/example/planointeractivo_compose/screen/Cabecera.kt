package com.example.planointeractivo_compose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planointeractivo_compose.Variante

/**
 * Cabecera de la pantalla. Segun la variante:
 *  - LibrarIA: degradado naranja redondeado abajo y titulo de marca grande.
 *  - Morada: barra plana de color solido con franja de acento, sin titulo.
 */
@Composable
fun Cabecera(
    detalle: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    if (Variante.cabeceraPlana) {
        Column(modifier = modifier.fillMaxWidth().background(Variante.oscuro)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                } else {
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = detalle.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    letterSpacing = 1.sp
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Variante.acento)
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(Brush.horizontalGradient(listOf(Variante.oscuro, Variante.primario, Variante.medio)))
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
            } else {
                Spacer(Modifier.width(8.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoStories,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = Variante.titulo,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    )
                }
                Text(text = detalle, color = Color.White.copy(alpha = 0.92f), fontSize = 13.sp)
            }
        }
    }
}

/** Tarjeta con la forma de la variante: elevada y redonda, o plana con borde y esquinas rectas. */
@Composable
fun Tarjeta(
    containerColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Variante.radioTarjeta),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (Variante.tarjetaConBorde) BorderStroke(1.5.dp, Variante.primario) else null,
        elevation = CardDefaults.cardElevation(if (Variante.tarjetaConBorde) 0.dp else 4.dp),
        content = content
    )
}
