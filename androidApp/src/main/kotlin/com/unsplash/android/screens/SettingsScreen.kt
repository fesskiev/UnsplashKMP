package com.unsplash.android.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unsplash.android.components.SettingsTopAppBar
import com.unsplash.android.theme.AppTheme
import com.unsplash.shared.presentation.settings.SettingsContract.State
import com.unsplash.shared.presentation.settings.SettingsContract.Event
import com.unsplash.shared.presentation.settings.SettingsViewModel
import com.unsplash.shared.data.utils.settings.ThemeMode
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    isExpandedScreen: Boolean,
    onOpenDrawerClick: () -> Unit
) {

    val viewModel = getViewModel<SettingsViewModel>()
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()

    SettingsContent(
        isExpandedScreen,
        uiState,
        onModeSelected = { viewModel.sendViewEvent(Event.AppThemeChangedAction(it)) },
        onOpenDrawerClick
    )
}

@Composable
private fun SettingsContent(
    isExpandedScreen: Boolean,
    uiState: State,
    onModeSelected: (ThemeMode) -> Unit,
    onOpenDrawerClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                isExpandedScreen,
                onOpenDrawerClick
            )
        }
    ) {
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(12.dp)
            )
            ThemeModeContent(
                uiState.themeMode,
                onModeSelected = onModeSelected
            )
        }
    }
}

@Composable
fun ThemeModeContent(
    selectedMode: ThemeMode?,
    onModeSelected: (ThemeMode) -> Unit
) {
    val themeModes = ThemeMode.values()
    Column {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 8.dp)
        )
        Column(Modifier.selectableGroup()) {
            themeModes.forEach { themeMode ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (themeMode == selectedMode),
                            onClick = { onModeSelected(themeMode) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (themeMode == selectedMode),
                        onClick = null
                    )
                    Text(
                        text = themeMode.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PhotoSettingsScreenPreview() {
    var uiState by remember {
        mutableStateOf(State(themeMode = ThemeMode.NIGHT))
    }
    AppTheme {
        Surface {
            SettingsContent(
                isExpandedScreen = false,
                uiState = uiState,
                onModeSelected = { uiState = State(themeMode = it) },
                onOpenDrawerClick = { }
            )
        }
    }
}