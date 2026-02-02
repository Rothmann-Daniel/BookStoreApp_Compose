package com.danielrothmann.bookstoreapp.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielrothmann.bookstoreapp.ui.theme.buttonIsenabled
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(modifier: Modifier) {
    val context = LocalContext.current
    // Initialize Firebase Auth
    val auth = Firebase.auth
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Log.d("auth", "LoginScreen: ${auth.currentUser?.uid}")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                signInWithEmailAndPassword(
                    auth,
                    email = emailState.value,
                    password = passwordState.value,
                    context = context
                )
            },
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonIsenabled, // Основной цвет кнопки
                contentColor = Color.White,  // Цвет текста/иконок
                disabledContainerColor = Color.Gray, // Цвет когда disabled
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                singUpWithEmailAndPassword(
                    auth,
                    email = emailState.value,
                    password = passwordState.value,
                    context = context
                )
            },
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonIsenabled, // Основной цвет кнопки
                contentColor = Color.White,  // Цвет текста/иконок
                disabledContainerColor = Color.Gray, // Цвет когда disabled
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Sign Up")
        }


    }
}

private fun singUpWithEmailAndPassword(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                val user = auth.currentUser

                Toast.makeText(
                    context,
                    "Регистрация успешна!",
                    Toast.LENGTH_SHORT,
                ).show()

                // Дополнительные действия после успешной регистрации
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Log.d("auth", "Verification email sent.")
                        }
                    }
                // updateUI(user)
            } else {
                Log.w("auth", "createUserWithEmail:failure", task.exception)

                // Более информативные сообщения об ошибках
                val errorMessage = when (task.exception) {
                    is FirebaseAuthWeakPasswordException -> "Слишком слабый пароль"
                    is FirebaseAuthInvalidCredentialsException -> "Неверный формат email"
                    is FirebaseAuthUserCollisionException -> "Пользователь уже существует"
                    else -> "Ошибка регистрации: ${task.exception?.message}"
                }

                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG,
                ).show()
                // updateUI(null)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("auth", "Registration failed: ", exception)
        }
}


private fun signInWithEmailAndPassword(auth: FirebaseAuth, email: String, password: String, context: Context) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("auth", "signInWithEmail:success")
                val user = auth.currentUser
            } else {
                Log.w("auth", "signInWithEmail:failure", task.exception)

            }
        }
}