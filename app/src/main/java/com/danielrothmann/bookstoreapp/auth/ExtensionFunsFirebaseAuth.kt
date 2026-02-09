package com.danielrothmann.bookstoreapp.auth

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser

// Extension функции для FirebaseAuth
fun FirebaseAuth.signUpWithEmail(
    email: String,
    password: String,
    context: Context,
    onSuccess: (FirebaseUser?) -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = currentUser
                onSuccess(user)

                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Письмо для подтверждения отправлено на email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                Toast.makeText(
                    context,
                    "Регистрация успешна!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val errorMessage = when (task.exception) {
                    is FirebaseAuthWeakPasswordException -> "Слишком слабый пароль"
                    is FirebaseAuthInvalidCredentialsException -> "Неверный формат email"
                    is FirebaseAuthUserCollisionException -> "Пользователь уже существует"
                    else -> "Ошибка регистрации: ${task.exception?.message}"
                }

                onFailure(errorMessage)
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
}

fun FirebaseAuth.signInWithEmail(
    email: String,
    password: String,
    context: Context,
    onSuccess: (FirebaseUser?) -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = currentUser
                onSuccess(user)
                Toast.makeText(
                    context,
                    "Вход выполнен успешно!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val errorMessage = when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> "Неверный email или пароль"
                    is FirebaseAuthInvalidUserException -> "Пользователь не найден"
                    else -> "Ошибка входа: ${task.exception?.message}"
                }

                onFailure(errorMessage)
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
}

fun FirebaseAuth.signOutWithToast(context: Context, onSuccess: () -> Unit = {}) {
    try {
        this.signOut()
        onSuccess()
        Toast.makeText(
            context,
            "Вы вышли из аккаунта",
            Toast.LENGTH_SHORT
        ).show()
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Ошибка при выходе: ${e.message}",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun FirebaseAuth.deleteAccountWithReauth(
    email: String,
    password: String,
    context: Context,
    onSuccess: () -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    val credential = EmailAuthProvider.getCredential(email, password)
    currentUser?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
        if (reauthTask.isSuccessful) {
            currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                if (deleteTask.isSuccessful) {
                    onSuccess()
                    Toast.makeText(
                        context,
                        "Аккаунт успешно удалён",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val error = "Ошибка удаления аккаунта: ${deleteTask.exception?.message}"
                    onFailure(error)
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val error = "Ошибка повторной аутентификации: ${reauthTask.exception?.message}"
            onFailure(error)
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}

// Проверка, авторизован ли пользователь
val FirebaseAuth.isUserSignedIn: Boolean
    get() = currentUser != null

// Получение email текущего пользователя
val FirebaseAuth.currentUserEmail: String?
    get() = currentUser?.email