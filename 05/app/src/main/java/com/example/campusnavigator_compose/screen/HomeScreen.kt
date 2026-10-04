package com.example.campusnavigator_compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.campusnavigator_compose.ui.theme.PurpleGradientEnd
import com.example.campusnavigator_compose.ui.theme.PurpleGradientStart
import com.example.campusnavigator_compose.viewmodel.SeleccionViewModel

@Composable
fun HomeScreen(viewModel: SeleccionViewModel) {
    val edificioSeleccionado by viewModel.edificioSeleccionado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(listOf(PurpleGradientStart, PurpleGradientEnd)),
                    RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
        ) {
            Text(
                text = "Bienvenido a la app de Edificios",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "Último edificio consultado",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = edificioSeleccionado,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
