package com.danielrothmann.bookstoreapp.book

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.data.Category
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    isAdmin: Boolean = false,
    onBack: () -> Unit = {},
    onBookUpdated: () -> Unit = {},
    onEditClick: (Book) -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }

    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    // Состояние для текущей книги (обновляется при изменении)
    var currentBook by remember { mutableStateOf(book) }

    // Состояния для редактирования
    var editTitle by remember { mutableStateOf(currentBook.title) }
    var editAuthor by remember { mutableStateOf(currentBook.author) }
    var editDescription by remember { mutableStateOf(currentBook.description) }
    var editPrice by remember { mutableStateOf(currentBook.price.toString()) }
    var editCategory by remember { mutableStateOf(currentBook.category) }
    var editImageBase64 by remember { mutableStateOf(currentBook.imageUrl) }

    // Обновляем состояния при изменении входной книги
    LaunchedEffect(book) {
        currentBook = book
        editTitle = book.title
        editAuthor = book.author
        editDescription = book.description
        editPrice = book.price.toString()
        editCategory = book.category
        editImageBase64 = book.imageUrl
    }

    // Состояния для категорий из Firestore
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoadingCategories by remember { mutableStateOf(true) }

    fun loadCategories() {
        isLoadingCategories = true
        db.collection("categories")
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

    // Декодируем bitmap для режима просмотра
    val bitmap = remember(currentBook.imageUrl) {
        if (currentBook.imageUrl.isNotBlank()) {
            runCatching {
                val bytes = Base64.decode(currentBook.imageUrl, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }.getOrNull()
        } else null
    }

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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.img_bg_mainscreen),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEditing) "Редактирование" else currentBook.title,
                            color = Color.White,
                            maxLines = 1
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (isEditing) {
                                // Сброс всех полей при отмене
                                editTitle = currentBook.title
                                editAuthor = currentBook.author
                                editDescription = currentBook.description
                                editPrice = currentBook.price.toString()
                                editCategory = currentBook.category
                                editImageBase64 = currentBook.imageUrl
                                isEditing = false
                            } else {
                                onBack()
                            }
                        }) {
                            Icon(
                                imageVector = if (isEditing) Icons.Default.Close
                                else Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = if (isEditing) "Cancel" else "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        if (isAdmin && !isEditing) {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isEditing) {

                    // --- РЕЖИМ РЕДАКТИРОВАНИЯ ---

                    // Пикер обложки
                    ImagePickerCard(
                        imageBase64 = editImageBase64.ifBlank { null },
                        onImageSelected = { editImageBase64 = it },
                        onImageRemoved = { editImageBase64 = "" }
                    )

                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Название") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editAuthor,
                        onValueChange = { editAuthor = it },
                        label = { Text("Автор") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )

                    // Дропдаун для категории
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
                            selectedCategory = editCategory,
                            onCategoryClick = { editCategory = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = editPrice,
                        onValueChange = { editPrice = it },
                        label = { Text("Цена") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Описание") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        minLines = 4
                    )

                    Button(
                        onClick = {
                            isSaving = true
                            val updatedBook = currentBook.copy(
                                title = editTitle,
                                author = editAuthor,
                                description = editDescription,
                                price = editPrice.toDoubleOrNull() ?: currentBook.price,
                                category = editCategory,
                                imageUrl = editImageBase64
                            )
                            db.updateBook(
                                bookId = currentBook.id,
                                updatedBook = updatedBook,
                                oldCategory = currentBook.category,
                                context = context,
                                onSuccess = {
                                    isSaving = false
                                    isEditing = false
                                    // Обновляем текущую книгу
                                    currentBook = updatedBook
                                    // Уведомляем MainScreen
                                    onBookUpdated()
                                },
                                onFailure = {
                                    isSaving = false
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Сохранить",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                } else {

                    // --- РЕЖИМ ПРОСМОТРА ---

                    // Обложка
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = currentBook.title,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }

                    // Название, автор, цена, категория
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = currentBook.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentBook.author,
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${currentBook.price} ₽",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = currentBook.category,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Описание
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Описание",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentBook.description,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Кнопка купить только для пользователя
                    if (!isAdmin) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = "Купить за ${currentBook.price} ₽",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Кнопка удалить только для админов
                    if (isAdmin) {
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red.copy(alpha = 0.8f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Удалить книгу",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Удалить книгу?") },
                                text = { Text("Книга \"${currentBook.title}\" будет удалена безвозвратно.") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            showDeleteDialog = false
                                            db.deleteBook(
                                                bookId = currentBook.id,
                                                bookCategory = currentBook.category,
                                                context = context,
                                                onSuccess = {
                                                    onBack()
                                                }
                                            )
                                        }
                                    ) {
                                        Text("Удалить", color = Color.Red)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDeleteDialog = false }) {
                                        Text("Отмена")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}