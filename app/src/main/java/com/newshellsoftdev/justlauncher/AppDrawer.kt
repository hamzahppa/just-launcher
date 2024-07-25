package com.newshellsoftdev.justlauncher

import android.content.pm.ApplicationInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*

@Composable
fun AppDrawer(
    apps: List<ApplicationInfo>,
    onAppClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()

    val filteredApps = if (searchQuery.text.isEmpty()) {
        apps
    } else {
        apps.filter { it.loadLabel(LocalContext.current.packageManager).toString().contains(searchQuery.text, ignoreCase = true) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column {
            CustomTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Search apps",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(16.dp)
            ) {
                items(filteredApps) { app ->
                    Text(
                        text = app.loadLabel(LocalContext.current.packageManager).toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onAppClick(app.packageName) }
                    )
                }
            }
        }
    }
}