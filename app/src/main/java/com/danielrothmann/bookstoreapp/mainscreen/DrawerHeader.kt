package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.danielrothmann.bookstoreapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.ui.theme.backgroundDrawer


@Composable
fun DrawerHeader(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(color = backgroundDrawer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        Image(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .height(100.dp),
            painter = painterResource(id = R.drawable.vintage_books),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit

        )
        Text(
            text = stringResource(R.string.greeting_welcome),
            color = Color.Black,
            fontFamily = FontFamily.Cursive,
            fontSize = 24.sp,
            modifier = Modifier
                .offset(y = (-16).dp) // Поднимаем текст вверх
        )
      }

}


@Preview(showBackground = true)
@Composable
fun DrawerHeaderPreview() {
    DrawerHeader()
}