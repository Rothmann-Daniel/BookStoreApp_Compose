package com.danielrothmann.bookstoreapp.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.book.BookCard
import com.danielrothmann.bookstoreapp.book.BookDetailScreen
import com.danielrothmann.bookstoreapp.data.Book
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val favoriteBooks by viewModel.favoriteBooks.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var selectedBook by remember { mutableStateOf<Book?>(null) }

    if (selectedBook != null) {
        BookDetailScreen(
            book = selectedBook!!,
            isAdmin = false,
            onBack = { selectedBook = null },
            onBookUpdated = { },
            onEditClick = {}
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            // Фоновое изображение
            Image(
                painter = painterResource(id = R.drawable.img_bg_mainscreen),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Затемнение
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                favoriteBooks.isEmpty() -> {
                    // Заглушка для пустого списка
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favorites",
                            modifier = Modifier.size(120.dp),
                            tint = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Your Favorites",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No favorite books yet",
                            fontSize = 18.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                else -> {
                    // Список избранных книг
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 48.dp, bottom = 16.dp)
                    ) {
                        items(favoriteBooks, key = { it.id }) { book ->
                            BookCard(
                                book = book,
                                isFavorite = true,
                                onFavoriteClick = {
                                    viewModel.removeFromFavorites(book)
                                },
                                onClick = { selectedBook = book }
                            )
                        }
                    }
                }
            }
        }
    }
}