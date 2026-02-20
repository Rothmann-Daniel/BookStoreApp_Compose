package com.danielrothmann.bookstoreapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.danielrothmann.bookstoreapp.navigation.NavGraph

@Composable
fun BookStoreApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}