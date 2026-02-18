package com.danielrothmann.bookstoreapp.book

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImagePickerCard(
    imageBase64: String?,
    onImageSelected: (String) -> Unit,
    onImageRemoved: () -> Unit
) {
    val context = LocalContext.current

    // Launcher для выбора изображения
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val base64String = ImageUtils.uriToBase64(context, it)
            base64String?.let { base64 ->
                onImageSelected(base64)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (imageBase64 != null) 250.dp else 80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (imageBase64 != null) {
                Color.White.copy(alpha = 0.9f)
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            }
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        if (imageBase64 != null) {
            // Предпросмотр изображения
            Box(modifier = Modifier.fillMaxSize()) {
                val bitmap = remember(imageBase64) {
                    ImageUtils.base64ToBitmap(imageBase64)
                }

                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Book cover preview",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Кнопка удаления
                IconButton(
                    onClick = onImageRemoved,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Red.copy(alpha = 0.8f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove image",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Кнопка изменения
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Change Image")
                }
            }
        } else {
            // Кнопка добавления изображения
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        indication = ripple(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Add Book Cover",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}