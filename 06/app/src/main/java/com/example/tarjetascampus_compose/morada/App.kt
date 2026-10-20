package com.example.tarjetascampus_compose.morada

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tarjetascampus_compose.R

// =====================================================================
// VERSION MORADA: tarjetas horizontales (Row), tipografia SansSerif,
// lista vertical con LazyColumn.
// =====================================================================

private val MoradoOscuro = Color(0xFF4A148C)
private val MoradoMedio = Color(0xFF7B1FA2)
private val MoradoClaro = Color(0xFFF3E5F5)
private val MoradoPastel = Color(0xFFE1BEE7)
private val FondoPantalla = Color(0xFFFAF5FC)

// ---------------------------------------------------------------- Datos

data class Lugar(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    @DrawableRes val imagen: Int,
    val categoria: String
)

val edificios = listOf(
    Lugar(0, "Biblioteca Central", "Zona de estudio y préstamo de libros", R.drawable.biblioteca, "Estudio"),
    Lugar(1, "Comedor Universitario", "Servicio de alimentación para estudiantes", R.drawable.comedor, "Servicios"),
    Lugar(2, "Pabellón de Ingeniería", "Aulas y laboratorios de las escuelas de ingeniería", R.drawable.biblioteca, "Aulas"),
    Lugar(3, "Auditorio Principal", "Conferencias, ceremonias y eventos académicos", R.drawable.comedor, "Eventos")
)

// ----------------------------------------------------------- Navegacion

@Composable
fun AppMorada() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "edificios") {
        composable("edificios") {
            EdificiosScreen(onLugarClick = { lugar ->
                navController.navigate("detalle/${lugar.id}")
            })
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val lugar = edificios.first { it.id == entry.arguments?.getInt("id") }
            DetalleLugarScreen(
                nombre = lugar.nombre,
                descripcion = lugar.descripcion,
                imagen = painterResource(lugar.imagen),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// ------------------------------------------------------ Pantalla lista

@Composable
fun EdificiosScreen(onLugarClick: (Lugar) -> Unit) {
    var estilo by rememberSaveable { mutableIntStateOf(0) }
    val estilos = listOf("Lugar", "Compacta", "Etiqueta")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Rasgo distintivo: cabecera con degradado morado
        Box(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(listOf(MoradoOscuro, MoradoMedio, Color(0xFFBA68C8)))
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Explora el campus",
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                )
                Text(
                    text = "${edificios.size} edificios · toca una tarjeta para ver el detalle",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            estilos.forEachIndexed { i, titulo ->
                FilterChip(
                    selected = estilo == i,
                    onClick = { estilo = i },
                    label = { Text(titulo) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MoradoMedio,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            items(edificios) { lugar ->
                when (estilo) {
                    0 -> TarjetaLugar(
                        nombre = lugar.nombre,
                        descripcion = lugar.descripcion,
                        imagen = painterResource(lugar.imagen),
                        colorFondo = MoradoClaro,
                        onClick = { onLugarClick(lugar) }
                    )
                    1 -> TarjetaCompacta(
                        nombre = lugar.nombre,
                        imagen = painterResource(lugar.imagen),
                        onClick = { onLugarClick(lugar) }
                    )
                    else -> TarjetaEtiqueta(
                        nombre = lugar.nombre,
                        descripcion = lugar.descripcion,
                        categoria = lugar.categoria,
                        imagen = painterResource(lugar.imagen),
                        onClick = { onLugarClick(lugar) }
                    )
                }
            }
        }
    }
}

// ------------------------------------------------- Estilo 1: TarjetaLugar

/** Row: imagen circular a la izquierda + Column con textos a la derecha. */
@Composable
fun TarjetaLugar(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    colorFondo: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = colorFondo,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = imagen,
                contentDescription = nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = nombre,
                    color = MoradoOscuro,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = descripcion,
                    color = MoradoMedio,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// ----------------------------------------------- Estilo 2: TarjetaCompacta

/** Box: imagen de fondo + degradado morado inferior + nombre abajo a la izquierda. */
@Composable
fun TarjetaCompacta(
    nombre: String,
    imagen: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = imagen,
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MoradoOscuro.copy(alpha = 0.85f))
                    )
                )
        )
        Text(
            text = nombre,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}

// ------------------------------------------ Estilo 3 (reto): TarjetaEtiqueta

/**
 * Box (imagen + degradado + contenido) con una etiqueta de categoria en la
 * esquina superior derecha (Row con Icon + Text) y Column de textos abajo.
 */
@Composable
fun TarjetaEtiqueta(
    nombre: String,
    descripcion: String,
    categoria: String,
    imagen: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = imagen,
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        startY = 150f
                    )
                )
        )
        Surface(
            shape = RoundedCornerShape(50),
            color = MoradoPastel,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MoradoOscuro,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = categoria,
                    color = MoradoOscuro,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = nombre,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            Text(
                text = descripcion,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp
            )
        }
    }
}

// ------------------------------------------------------ Pantalla detalle

/** Misma Surface/colores/tipografia que TarjetaLugar, pero a tamano completo. */
@Composable
fun DetalleLugarScreen(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantalla)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MoradoOscuro
            )
        }
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MoradoClaro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Image(
                    painter = imagen,
                    contentDescription = nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = nombre,
                    color = MoradoOscuro,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = descripcion,
                    color = MoradoMedio,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp
                )
            }
        }
    }
}
