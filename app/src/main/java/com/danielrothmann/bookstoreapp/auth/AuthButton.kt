package com.danielrothmann.bookstoreapp.auth

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danielrothmann.bookstoreapp.ui.theme.buttonIsenabled

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(0.5f)
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonIsenabled,  // Основной цвет кнопки
            contentColor = Color.White,  // Цвет текста/иконок
            disabledContainerColor = Color.Gray,  // Цвет когда disabled
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}