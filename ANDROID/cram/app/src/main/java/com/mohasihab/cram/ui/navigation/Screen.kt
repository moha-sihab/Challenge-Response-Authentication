package com.mohasihab.cram.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home/{username}") {
        fun createRoute(username: String) = "home/${Uri.encode(username)}"
    }
}
