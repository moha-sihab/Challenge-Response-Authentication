package com.mohasihab.cram.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mohasihab.cram.ui.home.HomeScreen
import com.mohasihab.cram.ui.login.LoginScreen

@Composable
fun NavHost(snackBarHostState: SnackbarHostState,
            navController: NavHostController = rememberNavController(),
            innerPadding: PaddingValues) {

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            var username by remember { mutableStateOf("") }

            LoginScreen(
                modifier = Modifier.padding(innerPadding),
                snackBarHostState = snackBarHostState,
                onLoginSuccess = {
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
            HomeScreen(
                modifier = Modifier.padding(innerPadding),
                snackBarHostState = snackBarHostState,
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
