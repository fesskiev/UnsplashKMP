@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalPermissionsApi::class)

package com.unsplash.android

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.unsplash.android.screens.HomeScreen
import com.unsplash.android.theme.AppTheme
import com.unsplash.android.theme.isDarkTheme
import com.unsplash.shared.data.utils.settings.AppSettings
import com.unsplash.shared.data.utils.settings.ThemeMode
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appSettings: AppSettings by inject()
        var themeMode by mutableStateOf<ThemeMode?>(null)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appSettings.state.collect {
                    themeMode = it?.themeMode
                }
            }
        }
        setContent {
            AppTheme(
                darkTheme = themeMode.isDarkTheme()
            ) {
                val windowSizeClass = calculateWindowSizeClass(this)
                val isExpandedScreen =
                    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded &&
                            windowSizeClass.heightSizeClass != WindowHeightSizeClass.Compact
                Log.wtf("screen_size", windowSizeClass.toString())
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val permissionsState = rememberMultiplePermissionsState(createListOfPermissions())
                    val permissionsGranted = permissionsState.allPermissionsGranted
                    println("all permissions granted?: $permissionsGranted")
                    if (permissionsGranted) {
                        HomeScreen(isExpandedScreen)
                    } else {
                        GrantPermissionsContent(
                            onRequestPermissionsClick = {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun createListOfPermissions(): List<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        return permissions
    }

    @Composable
    private fun GrantPermissionsContent(onRequestPermissionsClick: () -> Unit) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("It's important to grant all permissions. Please, grant permissions.")
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(onClick = onRequestPermissionsClick) {
                Text("Request permission")
            }
        }
    }
}
