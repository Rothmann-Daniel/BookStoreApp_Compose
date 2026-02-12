package com.danielrothmann.bookstoreapp.bottommenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.danielrothmann.bookstoreapp.R

sealed class BottomMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector,              //  Может быть ImageVector или Int для drawable
    val selectedIcon: ImageVector
) {
    object Home : BottomMenuItem(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object Favorites : BottomMenuItem(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Filled.Favorite
    )

    object Profile : BottomMenuItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    )

}