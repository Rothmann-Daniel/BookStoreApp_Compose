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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(modifier: Modifier) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_bg_mainscreen),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.greeting_welcome),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Cursive
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp, top = 64.dp)
                    .fillMaxWidth()
            )

            RoundedCornerTextField(
                value = emailState.value,
                label = stringResource(R.string.email)
            ) {
                emailState.value = it
            }

            Spacer(modifier = Modifier.height(16.dp))
            RoundedCornerTextField(
                value = passwordState.value,
                label = stringResource(R.string.password)
            ) {
                passwordState.value = it
            }

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(R.string.sign_in),
                enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
                onClick = {
                    // Используем extension функцию
                    auth.signInWithEmail(
                        email = emailState.value,
                        password = passwordState.value,
                        context = context,
                        onSuccess = { user ->
                            Log.d("auth", "Sign In Successful: ${user?.email}")
                            // Навигация на главный экран
                        },
                        onFailure = { error ->
                            Log.e("auth", "Sign In Failed: $error")
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            AuthButton(
                text = stringResource(R.string.sign_up),
                enabled = emailState.value.isNotBlank() && passwordState.value.isNotBlank(),
                onClick = {
                    // Используем extension функцию
                    auth.signUpWithEmail(
                        email = emailState.value,
                        password = passwordState.value,
                        context = context,
                        onSuccess = { user ->
                            Log.d("auth", "Sign Up Successful: ${user?.email}")
                        },
                        onFailure = { error ->
                            Log.e("auth", "Sign Up Failed: $error")
                        }
                    )
                }
            )
        }

        Text(
            text = stringResource(R.string.design_by),
            style = TextStyle(
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = FontFamily.Cursive
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier)
}