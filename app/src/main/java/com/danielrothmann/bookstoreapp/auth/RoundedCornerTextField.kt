package com.danielrothmann.bookstoreapp.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RoundedCornerTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,

) {
    TextField(
        value = value,
        onValueChange = {onValueChange(it)},
        label = { Text(text = "$label", color = Color.Gray) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor =Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
    )
}