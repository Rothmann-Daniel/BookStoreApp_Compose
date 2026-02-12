package com.danielrothmann.bookstoreapp.bottommenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun BottomMenu() {
    val bottomMenuItem = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favorites,
        BottomMenuItem.Profile
    )

    val selectedItem = remember {
        mutableStateOf("Home")
    }
}