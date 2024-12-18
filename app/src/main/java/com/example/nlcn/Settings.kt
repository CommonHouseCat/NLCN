package com.example.nlcn

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nlcn.ui.theme.NLCNTheme
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    // Custom class handles data persistence (Using DataStore)
    private val preferenceDataStore = PreferenceDataStore(application)

    // Save selected language to the datastore.
    suspend fun saveLanguage(language: String) {
        preferenceDataStore.saveLanguage(language)
    }

    // Save selected theme to the datastore.
    suspend fun saveTheme(theme: String) {
        preferenceDataStore.saveTheme(theme)
    }

    fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(viewModel: SettingsViewModel = viewModel()){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = remember { PreferenceDataStore(context) }
    val currentLanguage = dataStore.getLanguage.collectAsState(initial = "en")
    val currentTheme = dataStore.getTheme.collectAsState(initial = "dark")

    // Update configuration when language changes
    val updatedContext = remember(currentLanguage.value) {
        viewModel.updateLocale(context, currentLanguage.value)
    }

    NLCNTheme(dataStore = dataStore) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
        ) {
            TopAppBar(
                title = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = with(updatedContext) { getString(R.string.settings) },
                            color =  MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row for Theme settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = with(updatedContext) { getString(R.string.theme) },
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (currentTheme.value == "dark") Color.Gray else Color.LightGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                scope.launch { viewModel.saveTheme("dark") }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            with(updatedContext) { getString(R.string.DarkMode) },
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (currentTheme.value == "light") Color.Gray else Color.DarkGray,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                scope.launch { viewModel.saveTheme("light") }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            with(updatedContext) { getString(R.string.LightMode) },
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // Row for Language settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = with(updatedContext) { getString(R.string.language) },
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                when (currentLanguage.value) {
                                    "en" -> Color.Gray
                                    "vi" -> if (currentTheme.value == "dark") Color.DarkGray else Color.LightGray
                                    else -> Color.Gray
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                scope.launch { viewModel.saveLanguage("en") }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "En", color = MaterialTheme.colorScheme.onPrimary)
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                when (currentLanguage.value) {
                                    "vi" -> Color.Gray
                                    "en" -> if (currentTheme.value == "dark") Color.DarkGray else Color.LightGray
                                    else -> Color.Gray
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                scope.launch {
                                    viewModel.saveLanguage("vi")
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "Vi", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            // Row for About Page
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, AboutActivity::class.java)
                        context.startActivity(intent)
                    },
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = with(updatedContext) { getString(R.string.about) },
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .padding(vertical = 18.dp)
                )

                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "About",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

}
