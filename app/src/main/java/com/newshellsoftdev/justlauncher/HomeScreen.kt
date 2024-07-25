package com.newshellsoftdev.justlauncher

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: LauncherViewModel, onAppDrawerButtonClicked: () -> Unit) {
    var showAppSelection by remember { mutableStateOf(false) }
    val fastAccessApps by remember { viewModel.fastAccessApps }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        // Do nothing on back press
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showAppSelection = true }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, dragAmount ->
                        if (dragAmount.y < -30) {
                            coroutineScope.launch {
                                onAppDrawerButtonClicked()
                            }
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClockWidget(modifier = Modifier.padding(16.dp).padding(top = 160.dp))
            Spacer(modifier = Modifier.height(16.dp))
            FastAccessWidget(
                apps = fastAccessApps,
                onRemoveApp = { app -> viewModel.removeFastAccessApp(app) }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            IconButton(
                onClick = onAppDrawerButtonClicked,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(50)
                    )
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_apps_24),
                    contentDescription = "Open App Drawer",
                    tint = Color.White
                )
            }
        }
    }

    if (showAppSelection) {
        AppSelectionDialog(
            apps = viewModel.apps.value,
            onAppSelected = { selectedApps ->
                selectedApps.forEach { app ->
                    viewModel.addFastAccessApp(app)
                }
                showAppSelection = false
            },
            onDismissRequest = { showAppSelection = false }
        )
    }
}