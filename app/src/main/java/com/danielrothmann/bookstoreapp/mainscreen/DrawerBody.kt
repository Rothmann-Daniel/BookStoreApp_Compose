package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.book.CategoryRepository

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit = {},
    categoryRepo: CategoryRepository
) {
    // Список категорий из репозитория
    val categoriesList = categoryRepo.allCategories


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.img_box_bg_drawerbody),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f),
            contentScale = ContentScale.FillBounds
        )

        // Затемнение поверх изображения
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        // Контент (заголовок + список)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Заголовок
            Text(
                text = "Categories",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Список категорий
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Категории
                items(categoriesList) { category ->
                    CategoryItem(
                        category = category,
                        onClick = { onCategoryClick(category) }
                    )
                }

                // Разделитель
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }


            }
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick,
                    indication = ripple(color = Color.White),
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(vertical = 12.dp),
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

@Preview
@Composable
fun DrawerBodyPreview() {
    DrawerBody(categoryRepo = CategoryRepository())
}