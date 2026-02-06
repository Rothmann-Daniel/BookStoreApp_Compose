package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielrothmann.bookstoreapp.R


@Composable
fun DrawerBody() {
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ){
//        Image(
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds,
//            painter = painterResource(id= R.drawable.img_bg_box), contentDescription = "background")

    }
    LazyColumn {
        items(categoriesList.size) { index ->
            Text(text = categoriesList[index])
        }

    }
}
@Preview
@Composable
fun DrawerBodyPreview() {
    DrawerBody()
}