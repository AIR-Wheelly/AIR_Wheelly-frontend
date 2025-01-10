package com.air_wheelly.wheelly.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.air_wheelly.wheelly.util.NavigationItem

@Composable
fun BottomNavigation (
    navController: NavController,
    modifier : Modifier
) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Chat,
        NavigationItem.History,
        NavigationItem.Profile
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title)
                    )
                },
                label = {
                    Text(text = stringResource(id = item.title))
                },
                selected = currentDestination == item.route,
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}