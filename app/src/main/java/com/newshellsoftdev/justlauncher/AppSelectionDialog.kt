package com.newshellsoftdev.justlauncher

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*

@Composable
fun AppSelectionDialog(
    apps: List<ApplicationInfo>,
    onAppSelected: (List<ApplicationInfo>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val selectedApps = remember { mutableStateListOf<ApplicationInfo>() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add to Fast Access") },
        text = {
            LazyColumn {
                items(apps) { app ->
                    val isChecked = selectedApps.contains(app)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) {
                                    selectedApps.remove(app)
                                } else {
                                    if (selectedApps.size < 5) {
                                        selectedApps.add(app)
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = app.loadLabel(context.packageManager).toString(),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                if (isChecked) {
                                    selectedApps.remove(app)
                                } else {
                                    if (selectedApps.size < 5) {
                                        selectedApps.add(app)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAppSelected(selectedApps.toList())
                onDismissRequest()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}