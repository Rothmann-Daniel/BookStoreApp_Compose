package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun MainScreen(modifier: Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContent = {
            DrawerHeader()
        }
    )  {

    }

}