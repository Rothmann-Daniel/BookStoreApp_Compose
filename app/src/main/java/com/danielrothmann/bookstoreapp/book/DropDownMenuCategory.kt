package com.danielrothmann.bookstoreapp.book

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DropDownMenuCategory(
    categoryRepo: CategoryRepository,
    selectedCategory: String = "",
    onCategoryClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var categories by remember { mutableStateOf(categoryRepo.allCategories) }
    var isLoading by remember { mutableStateOf(false) }
    val firestore = remember { FirebaseFirestore.getInstance() }

    // Загружаем актуальные категории из Firestore
    LaunchedEffect(Unit) {
        isLoading = true
        firestore.collection("categories")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { result ->
                categories = result.documents.mapNotNull {
                    it.getString("name")
                }.sorted()
                isLoading = false
            }
            .addOnFailureListener {
                // В случае ошибки используем локальные категории
                categories = categoryRepo.allCategories
                isLoading = false
            }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        // Поле выбора - теперь с прозрачным фоном как в OutlinedTextField
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.05f), // Прозрачный фон
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) // Граница как у полей
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory.isNotBlank()) selectedCategory else "Выберите категорию",
                    color = if (selectedCategory.isNotBlank()) Color.White else Color.White.copy(alpha = 0.6f),
                    fontSize = 16.sp
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        // Выпадающее меню - тоже с прозрачным фоном
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 400.dp)
                .background(Color(0xFF2A2A2A).copy(alpha = 0.95f)) // Темный полупрозрачный фон
                .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
        ) {
            if (categories.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Нет доступных категорий",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    },
                    onClick = { expanded = false }
                )
            } else {
                categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        },
                        onClick = {
                            onCategoryClick(category)
                            expanded = false
                        }
                    )

                    // Разделитель
                    if (index < categories.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            thickness = 1.dp,
                            color = Color.White.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}