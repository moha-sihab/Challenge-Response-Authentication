package com.mohasihab.cram.ui.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mohasihab.cram.ui.home.HomeScreen
import com.mohasihab.cram.ui.login.LoginScreen

@Composable
fun NavHost(navController: NavHostController = rememberNavController()) {
    val username = "" //change real username
    val startDestination = if (true) { //check login here
        Screen.Home.createRoute(username)
    } else {
        Screen.Login.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            var username by remember { mutableStateOf("") }

            LoginScreen(
                onLoginClick = {
                    // Simulate login
                    navController.navigate(Screen.Home.createRoute(username)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "User"

            HomeScreen(
                username = username,
                onBiometricAuthClick = { /* TODO: Biometric Prompt */ },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
