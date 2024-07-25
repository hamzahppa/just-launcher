package com.newshellsoftdev.justlauncher

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun FastAccessWidget(apps: List<ApplicationInfo>, onRemoveApp: (ApplicationInfo) -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var appToRemove by remember { mutableStateOf<ApplicationInfo?>(null) }

    Column {
        apps.forEach { app ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                                if (launchIntent != null) {
                                    context.startActivity(launchIntent)
                                }
                            },
                            onLongPress = {
                                appToRemove = app
                                showDialog = true
                            }
                        )
                    }
            ) {
                Text(
                    text = app.loadLabel(context.packageManager).toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        appToRemove?.let { onRemoveApp(it) }
                        showDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Set the desired color here
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary // Set the desired color here
                    )
                ) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Remove App") },
            text = { Text("Are you sure you want to remove this app from Fast Access?") }
        )
    }
}