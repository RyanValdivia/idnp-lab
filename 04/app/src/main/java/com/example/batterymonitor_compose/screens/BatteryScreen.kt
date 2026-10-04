package com.example.batterymonitor_compose.screens

import androidx.compose.runtime.Composable
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batterymonitor_compose.ui.theme.Purple40
import com.example.batterymonitor_compose.ui.theme.Purple80

private const val ACCION_ACTUALIZAR_BATERIA = "com.tuapp.ACTUALIZAR_BATERIA"

private fun registrarReceiver(context: Context, receiver: BroadcastReceiver, filtro: IntentFilter) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, filtro, Context.RECEIVER_NOT_EXPORTED)
    } else {
        context.registerReceiver(receiver, filtro)
    }
}

@Composable
fun BatteryScreen(modifier: Modifier = Modifier) {
    var porcentaje by remember { mutableStateOf(0) }
    var cargando by remember { mutableStateOf(false) }

    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receiverSistema = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val nivel = intent?.getIntExtra(
                    BatteryManager.EXTRA_LEVEL, -1
                ) ?: -1

                val escala = intent?.getIntExtra(
                    BatteryManager.EXTRA_SCALE, -1
                ) ?: -1

                val estado = intent?.getIntExtra(
                    BatteryManager.EXTRA_STATUS, -1
                ) ?: -1

                if (nivel != -1 && escala != -1) {
                    porcentaje = nivel * 100 / escala
                }

                cargando = estado == BatteryManager.BATTERY_STATUS_CHARGING ||
                    estado == BatteryManager.BATTERY_STATUS_FULL
            }
        }

        val receiverActualizacionManual = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val batteryManager = context?.getSystemService(Context.BATTERY_SERVICE)
                    as? BatteryManager ?: return

                val nivelActual = batteryManager.getIntProperty(
                    BatteryManager.BATTERY_PROPERTY_CAPACITY
                )

                if (nivelActual in 0..100) {
                    porcentaje = nivelActual
                }

                cargando = batteryManager.isCharging

                Log.d("BatteryScreen", "Actualización manual: $nivelActual% cargando=$cargando")
            }
        }

        registrarReceiver(context, receiverSistema, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        registrarReceiver(context, receiverActualizacionManual, IntentFilter(ACCION_ACTUALIZAR_BATERIA))
        Log.d("BatteryScreen", "Receivers registrados")

        onDispose {
            context.unregisterReceiver(receiverSistema)
            context.unregisterReceiver(receiverActualizacionManual)
            Log.d("BatteryScreen", "Receivers desregistrados")
        }
    }

    val progresoAnimado by animateFloatAsState(
        targetValue = porcentaje / 100f,
        animationSpec = tween(durationMillis = 600),
        label = "progresoBateria"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(220.dp),
                strokeWidth = 14.dp,
                color = Purple80,
            )
            CircularProgressIndicator(
                progress = { progresoAnimado },
                modifier = Modifier.size(220.dp),
                strokeWidth = 14.dp,
                color = Purple40,
            )
            Text(
                text = "$porcentaje%",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Purple40
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (cargando) "Cargando" else "No cargando",
            fontSize = 16.sp,
            color = Purple40
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(ACCION_ACTUALIZAR_BATERIA).apply {
                setPackage(context.packageName)
            }

            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, flags
            )

            pendingIntent.send()
        }) {
            Text("Actualizar manualmente")
        }
    }
}
