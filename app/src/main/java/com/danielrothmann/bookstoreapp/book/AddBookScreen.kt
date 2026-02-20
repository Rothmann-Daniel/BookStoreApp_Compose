package com.danielrothmann.bookstoreapp.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.data.Category
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
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
    val imageBase64State = remember { mutableStateOf<String?>(null) }
    val priceState = remember { mutableStateOf("") }

    // Состояния для категорий из Firestore
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoadingCategories by remember { mutableStateOf(true) }

    // Цвета для полей ввода как в BookDetailScreen
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.White.copy(alpha = 0.8f),
        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
        cursorColor = Color.White,
        focusedContainerColor = Color.White.copy(alpha = 0.1f),
        unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
    )

    fun loadCategories() {
        isLoadingCategories = true
        firestore.collection("categories")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { result ->
                categories = result.documents.mapNotNull { document ->
                    Category(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        bookCount = document.getLong("bookCount")?.toInt() ?: 0,
                        isActive = document.getBoolean("isActive") ?: true
                    )
                }.sortedBy { it.name }
                isLoadingCategories = false
            }
            .addOnFailureListener {
                categories = emptyList()
                isLoadingCategories = false
            }
    }

    // Загружаем категории из Firestore
    LaunchedEffect(Unit) {
        loadCategories()
    }

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

            // Category
            if (isLoadingCategories) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                DropDownMenuCategory(
                    categories = categories.map { it.name },
                    selectedCategory = categoryState.value,
                    onCategoryClick = { selectedCategory ->
                        categoryState.value = selectedCategory
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title OutlinedTextField с прозрачным фоном
            OutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Author
            OutlinedTextField(
                value = authorState.value,
                onValueChange = { authorState.value = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            OutlinedTextField(
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                minLines = 4,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            OutlinedTextField(
                value = priceState.value,
                onValueChange = { priceState.value = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image Picker
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
            Button(
                onClick = {
                    val book = Book(
                        title = titleState.value,
                        author = authorState.value,
                        description = descriptionState.value,
                        category = categoryState.value,
                        imageUrl = imageBase64State.value ?: "",
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = titleState.value.isNotBlank() &&
                        authorState.value.isNotBlank() &&
                        categoryState.value.isNotBlank() &&
                        priceState.value.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    text = "Add Book",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Book Store Admin",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Cursive
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}