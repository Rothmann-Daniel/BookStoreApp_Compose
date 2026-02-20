package com.danielrothmann.bookstoreapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.danielrothmann.bookstoreapp.auth.LoginScreen
import com.danielrothmann.bookstoreapp.book.AddBookScreen
import com.danielrothmann.bookstoreapp.category.CategoriesManagementScreen
import com.danielrothmann.bookstoreapp.favourites.FavoritesScreen
import com.danielrothmann.bookstoreapp.mainscreen.MainScreen
import com.danielrothmann.bookstoreapp.profile.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object AddBook : Screen("add_book")
    object Categories : Screen("categories")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                modifier = modifier,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            MainScreen(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                // MainScreen сам создает categoryRepo внутри, не нужно передавать
            )
        }

        composable(Screen.Favorites.route) {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    com.danielrothmann.bookstoreapp.bottommenu.BottomMenu(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                },
                contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    FavoritesScreen()
                }
            }
        }

        composable(Screen.Profile.route) {
            androidx.compose.material3.Scaffold(
                bottomBar = {
                    com.danielrothmann.bookstoreapp.bottommenu.BottomMenu(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                },
                contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ProfileScreen(
                        onSignOut = {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToAddBook = {
                            navController.navigate(Screen.AddBook.route)
                        },
                        onNavigateToCategories = { // ДОБАВЬТЕ ЭТУ СТРОКУ
                            navController.navigate(Screen.Categories.route)
                        }
                    )
                }
            }
        }

        //  Передаем categoryRepo в AddBookScreen
        composable(Screen.AddBook.route) {
            AddBookScreen(
                onBookAdded = {
                    navController.popBackStack()
                }
            )
        }

        // экран для управления категориями
        composable(Screen.Categories.route) {
            CategoriesManagementScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}