package com.newshellsoftdev.justlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.newshellsoftdev.justlauncher.ui.theme.JustLauncherTheme

class MainActivity : ComponentActivity() {
    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start Foreground Service
        val intent = Intent(this, ForegroundService::class.java)
        startService(intent)

        setContent {
            JustLauncherTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Black,
                        darkIcons = false // Set to true if you want dark icons on the status bar
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val isAppDrawerOpen = rememberSaveable { mutableStateOf(false) }

                    val lifecycleOwner = ProcessLifecycleOwner.get()

                    DisposableEffect(lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_STOP) {
                                // Close AppDrawer when the app goes to background
                                isAppDrawerOpen.value = false
                                navController.popBackStack("home", inclusive = false)
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    DisposableEffect(navController) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START && isAppDrawerOpen.value) {
                                isAppDrawerOpen.value = false
                                navController.popBackStack("home", inclusive = false)
                            }
                        }
                        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
                        onDispose {
                            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
                        }
                    }

                    LauncherScreen(
                        viewModel = launcherViewModel,
                        navController = navController,
                        isAppDrawerOpen = isAppDrawerOpen
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop Foreground Service
        val intent = Intent(this, ForegroundService::class.java)
        stopService(intent)
    }
}