package com.air_wheelly.wheelly.util

import com.air_wheelly.wheelly.R

sealed class NavigationItem(val route: String, val icon: Int, val title: Int) {
    object Home : NavigationItem("carList", R.drawable.baseline_home_24, R.string.home)
    object Profile : NavigationItem("profile", R.drawable.baseline_person_2_24, R.string.profile)
    object Chat : NavigationItem("chat", R.drawable.baseline_chat_24, R.string.chat)
    object History : NavigationItem("history", R.drawable.baseline_history_24, R.string.history)
    object AddForRent : NavigationItem("createListing", R.drawable.baseline_add_box_24, R.string.addCar)
}