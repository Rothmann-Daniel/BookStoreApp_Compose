package com.danielrothmann.bookstoreapp.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.auth.AuthButton
import com.danielrothmann.bookstoreapp.auth.RoundedCornerTextField
import com.danielrothmann.bookstoreapp.data.Book
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddBookScreen(
    onBookAdded: () -> Unit = {}
) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    // Состояния для полей
    val titleState = remember { mutableStateOf("") }
    val authorState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val imageBase64State = remember { mutableStateOf<String?>(null) } // Base64
    val priceState = remember { mutableStateOf("") }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Add New Book",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Cursive
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            )

            // Title
            RoundedCornerTextField(
                value = titleState.value,
                label = "Title",
                onValueChange = { titleState.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Author
            RoundedCornerTextField(
                value = authorState.value,
                label = "Author",
                onValueChange = { authorState.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            RoundedCornerTextField(
                value = descriptionState.value,
                label = "Description",
                maxLines = 5,
                onValueChange = { descriptionState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category
            RoundedCornerTextField(
                value = categoryState.value,
                label = "Category",
                onValueChange = { categoryState.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            RoundedCornerTextField(
                value = priceState.value,
                label = "Price",
                onValueChange = { priceState.value = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image Picker с предпросмотром
            ImagePickerCard(
                imageBase64 = imageBase64State.value,
                onImageSelected = { base64 ->
                    imageBase64State.value = base64
                },
                onImageRemoved = {
                    imageBase64State.value = null
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Add Book Button
            AuthButton(
                text = "Add Book",
                enabled = titleState.value.isNotBlank() &&
                        authorState.value.isNotBlank() &&
                        priceState.value.isNotBlank() &&
                        imageBase64State.value != null, // Требуем изображение
                onClick = {
                    val book = Book(
                        title = titleState.value,
                        author = authorState.value,
                        description = descriptionState.value,
                        category = categoryState.value,
                        imageUrl = imageBase64State.value ?: "", // Сохраняем Base64
                        price = priceState.value.toDoubleOrNull() ?: 0.0
                    )

                    firestore.addBook(
                        book = book,
                        context = context,
                        onSuccess = { bookId ->
                            // Очищаем поля
                            titleState.value = ""
                            authorState.value = ""
                            descriptionState.value = ""
                            categoryState.value = ""
                            imageBase64State.value = null
                            priceState.value = ""

                            onBookAdded()
                        },
                        onFailure = { error ->
                            // Ошибка показана в Toast
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Text(
            text = "Book Store Admin",
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = FontFamily.Cursive
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
    }
}