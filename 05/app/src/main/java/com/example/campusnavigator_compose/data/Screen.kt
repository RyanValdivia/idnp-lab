package com.example.campusnavigator_compose.data

sealed class Screen(val route: String, val label: String) {
    object Home: Screen("home", "Home")
    object Edificios: Screen("edificios", "Edificios")
    object Mapa: Screen("mapa", "Mapa")
    object Perfil: Screen("perfil", "Perfil")
}