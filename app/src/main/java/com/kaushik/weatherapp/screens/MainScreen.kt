package com.kaushik.weatherapp.screens

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
import com.kaushik.weatherapp.roomDB.CityDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(db: CityDatabase) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Weather app") })
        },
        bottomBar = {
            MyBottomBar(navController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Search.route) {
                composable(Search.route) {
                    SearchScreen(navController, db)
                }

                composable(History.route) {
                    HistoryScreen(navController, db)
                }

                composable(Result.route) {
                    ResultScreen()
                }
            }
        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {
    val destinationList = listOf(
        Search, Result, History
    )

    val selectedIndex = rememberSaveable { mutableStateOf(0) }
    BottomNavigation {
        destinationList.forEachIndexed { index, destination ->
            BottomNavigationItem(
                selected = index == selectedIndex.value,
                onClick = {
                    selectedIndex.value = index
                    navController.navigate(destinationList[index].route) {
                        popUpTo(Search.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        modifier = Modifier.height(25.dp),
                        contentDescription = destination.title
                    )
                }
            )
        }
    }
}
