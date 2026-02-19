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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropDownMenuCategory(
    categories: List<String>,
    selectedCategory: String = "",
    onCategoryClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        // Поле выбора - с прозрачным фоном
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.05f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
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

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        // Выпадающее меню
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 400.dp)
                .background(Color(0xFF2A2A2A).copy(alpha = 0.95f))
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