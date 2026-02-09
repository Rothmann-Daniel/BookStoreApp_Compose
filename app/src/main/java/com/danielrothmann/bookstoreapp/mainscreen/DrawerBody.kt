package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R

@Composable
fun DrawerBody(modifier: Modifier) {
    val categoriesList = listOf(
        "Favorites",
        "Bestsellers",
        "Detective",
        "Novels",
        "Fiction",
        "Science and Technology",
        "Educational literature",
        "For Children"
    )

    Box(
        modifier = modifier // Используем переданный modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        // Фоновое изображение растянутое на весь экран
        Image(
            painter = painterResource(id = R.drawable.img_box_bg_drawerbody),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f), // 70% непрозрачности (30% прозрачности)
            contentScale = ContentScale.FillBounds
        )

        // Список поверх изображения
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(categoriesList) { category ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category,
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontSize = 22.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.3f))
                )
            }


        }
    }
}


@Preview
@Composable
fun DrawerBodyPreview() {
    DrawerBody(modifier = Modifier)
}