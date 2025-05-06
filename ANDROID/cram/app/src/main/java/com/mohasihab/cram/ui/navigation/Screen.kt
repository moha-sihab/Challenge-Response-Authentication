package com.mohasihab.cram.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home/{username}") {
        fun createRoute(username: String) = "home/$username"
    }
}
