// This is the package name for your app.
package com.kaushik.weatherapp

// Import the necessary libraries.
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// This annotation allows us to use the `ExperimentalMaterial3Api` API.
@OptIn(ExperimentalMaterial3Api::class)

// Composable functions are declared with the `@Composable` annotation.
@Composable
fun MainScreen() {
    // Create a NavController object to manage navigation between screens.
    val navController = rememberNavController()

    // Create a Scaffold layout to provide a top bar and bottom bar.
    Scaffold(
        topBar = {
            // Add a TopAppBar with the title "Weather app".
            TopAppBar(title = { Text(text = "Weather app") })
        },
        bottomBar = {
            // Add a custom bottom bar.
            MyBottomBar(navController)
        }
    ) {
        // Add a Box layout to pad the content inside the Scaffold.
        Box(modifier = Modifier.padding(it)) {
            // Add a NavHost to manage navigation between screens.
            NavHost(navController = navController, startDestination = Search.route) {
                // Add the SearchScreen as a composable function.
                composable(Search.route) {
                    SearchScreen(navController)
                }

                // Add the HistoryScreen as a composable function.
                composable(History.route) {
                    HistoryScreen(navController)
                }

                // Add the ResultScreen as a composable function.
                composable(Result.route) {
                    ResultScreen()
                }
            }
        }
    }
}

// This function creates a custom bottom bar.
@Composable
fun MyBottomBar(navController: NavHostController) {
    // Create a list of destinations.
    val destinationList = listOf(
        Search, Result, History
    )

    // Create a mutable state variable to store the selected destination index.
    val selectedIndex = rememberSaveable { mutableStateOf(0) }

    // Create a BottomNavigation layout.
    BottomNavigation {
        // Iterate over the list of destinations and add a BottomNavigationItem for each one.
        destinationList.forEachIndexed { index, destination ->
            BottomNavigationItem(
                selected = index == selectedIndex.value, // Set the item to selected if it is the current destination.
                onClick = {
                    // Update the selected destination index.
                    selectedIndex.value = index

                    // Navigate to the selected destination.
                    navController.navigate(destinationList[index].route) {
                        // Pop up to the Search screen.
                        popUpTo(Search.route)

                        // Launch the destination as a single top.
                        launchSingleTop = true
                    }
                },
                icon = {
                    // Add an icon for the destination.
                    Icon(
                        painter = painterResource(id = destination.icon), // Use the destination's icon resource.
                        modifier = Modifier.height(25.dp),
                        contentDescription = destination.title // Set the content description to the destination's title.
                    )
                }
            )
        }
    }
}
