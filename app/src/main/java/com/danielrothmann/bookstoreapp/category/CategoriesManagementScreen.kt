package com.danielrothmann.bookstoreapp.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.auth.RoundedCornerTextField
import com.danielrothmann.bookstoreapp.data.Category
import com.danielrothmann.bookstoreapp.category.CategoryFirestoreHelper.addCategory
import com.danielrothmann.bookstoreapp.category.CategoryFirestoreHelper.deleteCategory
import com.danielrothmann.bookstoreapp.category.CategoryFirestoreHelper.getAllCategories
import com.danielrothmann.bookstoreapp.category.CategoryFirestoreHelper.updateCategory
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesManagementScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val firestore = remember { FirebaseFirestore.getInstance() }

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Category?>(null) }



    fun loadCategories() {
        isLoading = true
        firestore.getAllCategories(
            onSuccess = { loadedCategories ->
                categories = loadedCategories
                isLoading = false
            },
            onFailure = {
                isLoading = false
            }
        )
    }

    // Загружаем категории
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

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Управление категориями",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Category",
                        tint = Color.White
                    )
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = { editingCategory = category },
                            onDelete = { showDeleteDialog = category }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    // Диалог добавления/редактирования
    if (showAddDialog || editingCategory != null) {
        CategoryDialog(
            category = editingCategory,
            onDismiss = {
                showAddDialog = false
                editingCategory = null
            },
            onSave = { name, description ->
                if (editingCategory != null) {
                    // Редактирование
                    firestore.updateCategory(
                        categoryId = editingCategory!!.id,
                        name = name,
                        description = description,
                        context = context,
                        onSuccess = {
                            loadCategories()
                            editingCategory = null
                        }
                    )
                } else {
                    // Добавление
                    val newCategory = Category(
                        name = name,
                        description = description
                    )
                    firestore.addCategory(
                        category = newCategory,
                        context = context,
                        onSuccess = {
                            loadCategories()
                            showAddDialog = false
                        }
                    )
                }
            }
        )
    }

    // Диалог удаления
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Удалить категорию?") },
            text = {
                Text("Категория \"${showDeleteDialog!!.name}\" будет удалена. Книги в этой категории останутся, но категория будет недоступна для выбора.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firestore.deleteCategory(
                            categoryId = showDeleteDialog!!.id,
                            context = context,
                            hardDelete = false // Мягкое удаление
                        ) {
                            loadCategories()
                            showDeleteDialog = null
                        }
                    }
                ) {
                    Text("Удалить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (category.description.isNotBlank()) {
                    Text(
                        text = category.description,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Счетчик книг
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${category.bookCount} книг",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Статус
                    if (!category.isActive) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Red.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "Неактивна",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Иконки действий с фоном
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Кнопка редактирования с фоном
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }

                // Кнопка удаления с фоном
                Surface(
                    shape = CircleShape,
                    color = Color.Red.copy(alpha = 0.3f),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(
    category: Category? = null,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var description by remember { mutableStateOf(category?.description ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (category != null) "Редактировать категорию" else "Новая категория",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название категории") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(name, description)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotBlank()
                    ) {
                        Text(if (category != null) "Сохранить" else "Добавить")
                    }
                }
            }
        }
    }
}
