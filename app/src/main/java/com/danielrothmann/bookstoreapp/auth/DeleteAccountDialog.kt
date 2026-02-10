package com.danielrothmann.bookstoreapp.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.danielrothmann.bookstoreapp.auth.RoundedCornerTextField

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (email: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Удаление аккаунта",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Для удаления аккаунта подтвердите свои данные:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                RoundedCornerTextField(
                    value = email,
                    label = "Email",
                    onValueChange = { email = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                RoundedCornerTextField(
                    value = password,
                    label = "Password",
                    isPassword = true,
                    onValueChange = { password = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                onConfirm(email, password)
                            }
                        },
                        enabled = email.isNotBlank() && password.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteAccountDialogPreview(){
    DeleteAccountDialog(onDismiss = {}, onConfirm = { _, _ -> })
}