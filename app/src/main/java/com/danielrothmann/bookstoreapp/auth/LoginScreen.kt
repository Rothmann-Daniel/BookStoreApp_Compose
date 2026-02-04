package com.danielrothmann.bookstoreapp.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(modifier: Modifier) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Log.d("auth", "LoginScreen: ${auth.currentUser?.email} UID: ${auth.currentUser?.uid}")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_bg_mainscreen),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Основной контент по центру
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Приветствие
            Text(
                text = "Welcome to Our BookStore...",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Cursive
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp, top = 64.dp).fillMaxWidth()
            )

            // Поля ввода
            RoundedCornerTextField(
                value = emailState.value,
                label = "Email"
            ) {
                emailState.value = it
            }

            Spacer(modifier = Modifier.height(16.dp))
            RoundedCornerTextField(
                value = passwordState.value,
                label = "Password"
            ) {
                passwordState.value = it
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопки
            AuthButton(
                text = "Sign In",
                enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
                onClick = {
                    signInWithEmailAndPassword(
                        auth,
                        email = emailState.value,
                        password = passwordState.value,
                        context = context
                    )
                    Log.d("auth", "Sing In Successful: ${auth.currentUser?.email} ")
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            AuthButton(
                text = "Sign Up",
                enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
                onClick = {
                    singUpWithEmailAndPassword(
                        auth,
                        email = emailState.value,
                        password = passwordState.value,
                        context = context
                    )
                    Log.d("auth", "Sing Up Successful: ${auth.currentUser?.email} ")
                }
            )
        }

        // Подпись
        Text(
            text = "Design by Daniel Rothmann",
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = FontFamily.Cursive
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter) // Прижимает к низу
                .padding(bottom = 32.dp) // Отступ от самого низа
                .fillMaxWidth()
        )
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


private fun signInWithEmailAndPassword(
    auth: FirebaseAuth,
    email: String,
    password: String,
    context: Context
) {
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


private fun deleteAccount(
    auth: FirebaseAuth,
    email: String,
    password: String
) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
        if (reauthTask.isSuccessful) {
            auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                if (deleteTask.isSuccessful) {
                    Log.d("auth", "User account deleted.")
                } else {
                    Log.w("auth", "User account deletion failed", deleteTask.exception)
                }
            }
        } else {
            Log.w("auth", "User reauthentication failed", reauthTask.exception)
        }
    }
}

private fun singOut(auth: FirebaseAuth) {
    auth.signOut()
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier)
}