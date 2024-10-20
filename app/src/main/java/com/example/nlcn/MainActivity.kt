package com.example.nlcn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nlcn.ui.theme.NLCNTheme

class MainActivity : ComponentActivity() {
    private val showBottomBarState = lazy { mutableStateOf(true) }
    var showBottomBar: Boolean
        get() = showBottomBarState.value.value
        set(value) { showBottomBarState.value.value = value }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Applies your app's theme to the content.
            NLCNTheme {
                Surface(
                    // Makes the surface fill the entire screen.
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.Black // Sets the background color of the surface to black.
                ) {
                    MyBottomNavBar() // Calls the composable function that creates the bottom navigation bar.
                }
            }
        }
    }
}

@Composable
fun MyBottomNavBar() {
    val navController = rememberNavController() // Creates a navigation controller to manage navigation between screens.
    val selected = remember { mutableStateOf(Icons.Default.Home) } // Stores the Home icon as the default selected navigation item.
    val mainActivity = LocalContext.current as MainActivity

    Scaffold(
        bottomBar = {
            if(mainActivity.showBottomBar) {
                Box( // Defines the box that contains the bottom bar.
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding() // Adds padding to avoid overlapping with system navigation bars.
                        .background(Color.Black)
                ) {
                    BottomAppBar( // Define the bottom bar itself
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)) // Clips the BottomAppBar to a rounded shape.
                            .fillMaxWidth(1f) // Makes the BottomAppBar fill the width of the screen.
                            .align(Alignment.BottomCenter)
                            .height(56.dp), // Sets the height of the BottomAppBar.
                        containerColor = Color(0xFF8031A7),
                        contentPadding = PaddingValues(0.dp)
                    ) {

                        // Icon button for the Home.kt file.
                        IconButton(
                            onClick = { // Similar to set on click listener.
                                selected.value = Icons.Default.Home // Set the selected icon to Home.kt.
                                // Open the Home activity if the Home icon is clicked.
                                navController.navigate(Screens.Home.screen) { // Force to return to the home screen on back press.
                                    popUpTo(Screens.Home.screen) // Pops up to the home screen in the back stack.
                                }
                            },
                            modifier = Modifier.weight(1f) // Assigns equal space between icons.
                        ) {
                            Icon( // Icon displays the icon for Home.kt.
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home screen",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Home)
                                    Color.White else Color.Black
                            )
                        }

                        // Icon button for the LocalFile.kt file.
                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.Folder
                                navController.navigate(Screens.LocalFile.screen) {
                                    popUpTo(Screens.Home.screen)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "Local File screen",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Folder)
                                    Color.White else Color.Black
                            )
                        }

                        // Icon button for the Settings.kt file.
                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.Settings
                                navController.navigate(Screens.Settings.screen) {
                                    popUpTo(Screens.Home.screen)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings screen",
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.Settings)
                                    Color.White else Color.Black
                            )
                        }
                    }
                }
            }

        }
    ) { paddingValues ->
        NavHost(
            navController = navController, // Pass the navController to NavHost.
            startDestination = Screens.Home.screen, // Set initial destination to Home.kt file.
            modifier = Modifier.padding(paddingValues) // Apply padding values to the NavHost.
        ) {
            // Composable functions defining the routes to different screens.
            composable(Screens.Home.screen) { Home(navController) } // Home.kt file.
            composable(Screens.LocalFile.screen) { LocalFile() } // LocalFile.kt file.
            composable(Screens.Settings.screen) { Settings() } // Settings.kt file.

            // Composable function defining the route to PlayPreLoadedSound.kt with 2 parameter (soundFileName and displayName).
            composable("play_preloaded_sound/{soundFileName}/{displayName}") { backStackEntry ->
                val soundFileName = backStackEntry.arguments?.getString("soundFileName") ?: return@composable
                val displayName = backStackEntry.arguments?.getString("displayName") ?: return@composable
                PlayPreLoadedSoundScreen(context = LocalContext.current, soundFileName = soundFileName, displayName = displayName)
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun GreetingPreview() {
    NLCNTheme {
        MyBottomNavBar()
    }
}