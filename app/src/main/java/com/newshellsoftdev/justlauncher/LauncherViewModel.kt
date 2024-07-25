package com.newshellsoftdev.justlauncher

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {
    val apps: MutableState<List<ApplicationInfo>> = mutableStateOf(emptyList())
    val fastAccessApps: MutableState<List<ApplicationInfo>> = mutableStateOf(emptyList())

    private val context = application.applicationContext
    private val packageManager: PackageManager = context.packageManager

    init {
        loadApps()
        loadFastAccessApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val availableApps = packageManager.queryIntentActivities(intent, 0).map {
                it.activityInfo.applicationInfo
            }
            apps.value = availableApps.sortedBy { it.loadLabel(packageManager).toString() }
        }
    }

    private fun saveFastAccessApps() {
        val sharedPreferences = context.getSharedPreferences("fast_access_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(fastAccessApps.value.map { it.packageName })
        editor.putString("fast_access_apps", json)
        editor.apply()
    }

    private fun loadFastAccessApps() {
        val sharedPreferences = context.getSharedPreferences("fast_access_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("fast_access_apps", null)
        if (json != null) {
            val packageNames: List<String> = gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
            fastAccessApps.value = packageNames.mapNotNull { packageName ->
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA).find { it.packageName == packageName }
            }
        }
    }

    fun addFastAccessApp(app: ApplicationInfo) {
        if (fastAccessApps.value.size < 5 && !fastAccessApps.value.contains(app)) {
            fastAccessApps.value = fastAccessApps.value + app
            saveFastAccessApps()
        }
    }

    fun removeFastAccessApp(app: ApplicationInfo) {
        fastAccessApps.value = fastAccessApps.value - app
        saveFastAccessApps()
    }

    fun launchApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.let { context.startActivity(it) }
    }
}