package com.newshellsoftdev.justlauncher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun LauncherScreen(viewModel: LauncherViewModel, navController: NavHostController) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Black,
            darkIcons = false
        )
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onAppDrawerButtonClicked = {
                    navController.navigate("appDrawer")
                }
            )
        }
        composable("appDrawer") {
            AppDrawer(
                apps = viewModel.apps.value,
                onAppClick = { packageName ->
                    viewModel.launchApp(packageName)
                    navController.popBackStack()
                }
            )
        }
    }
}