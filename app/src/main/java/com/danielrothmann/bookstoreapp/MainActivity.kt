package com.danielrothmann.bookstoreapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.danielrothmann.bookstoreapp.data.Book
import com.danielrothmann.bookstoreapp.ui.theme.BookStoreAppTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BookStoreAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fireStore = Firebase.firestore
    val firebaseStorage = Firebase.storage.reference // Используем корень Storage
    val listBooks = remember { mutableStateListOf<Book>() }
    var isLoading by remember { mutableStateOf(true) }
    var isUploading by remember { mutableStateOf(false) }

    // Используем DisposableEffect для управления слушателем
    DisposableEffect(Unit) {
        val listener = fireStore.collection("books")
            .addSnapshotListener { snapshot, error ->
                isLoading = false

                error?.let {
                    Log.e("Firestore", "Listen failed", it)
                    return@addSnapshotListener
                }

                snapshot?.let { querySnapshot ->
                    val books = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Book::class.java)
                    }
                    listBooks.clear()
                    listBooks.addAll(books)
                    Log.d("Firestore", "Books updated: ${books.size} books")
                }
            }

        onDispose {
            listener.remove()
            Log.d("Firestore", "Listener removed")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Text("Загрузка книг...")
            }
        } else if (listBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Text("Книг пока нет")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listBooks) { book ->
                    BookCard(book = book)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = !isUploading,
            onClick = {
                isUploading = true

                // Генерируем уникальное имя файла
                val fileName = "book_${UUID.randomUUID()}.png"

                // Загружаем в images/[имя_файла]
                val imageRef = firebaseStorage.child("images/$fileName")

                imageRef.putBytes(bitmapToByteArray(context))
                    .addOnSuccessListener { uploadTask ->
                        uploadTask.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                            Log.d("FirebaseStorage", "Image URL: $url")
                            saveBook(
                                fireStore = fireStore,
                                imageUrl = url.toString()
                            ) { success ->
                                isUploading = false
                                if (!success) {
                                    Log.e("FirebaseStorage", "Failed to save book info")
                                }
                            }
                        }?.addOnFailureListener { e ->
                            isUploading = false
                            Log.e("FirebaseStorage", "Failed to get download URL", e)
                        }
                    }
                    .addOnFailureListener { e ->
                        isUploading = false
                        Log.e("FirebaseStorage", "Error uploading image", e)
                    }
            }
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Загрузка...")
            } else {
                Text(text = "Добавить книгу")
            }
        }
    }
}


@Composable
fun BookCard(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Используем AsyncImage для загрузки изображений из URL
            if (book.imageUrl.isNotEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = book.imageUrl,
                    contentDescription = "Book cover",
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground)
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Book cover"
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о книге
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.author,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = book.category,
                    fontSize = 12.sp,
                    color = Color.Blue
                )
                Text(
                    text = book.description.take(50) + if (book.description.length > 50) "..." else "",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }

            // Цена
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "$${book.price}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BookStoreAppTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    BookStoreAppTheme {
        BookCard(
            book = Book(
                title = "Война и мир",
                author = "Лев Толстой",
                description = "Роман-эпопея, описывающий русское общество в эпоху войн против Наполеона",
                category = "Классика",
                imageUrl = "",
                price = 24.99
            )
        )
    }
}
// Firebase Storage работает с сырыми данными (bytes), а не с объектами Bitmap. Поэтому нужно конвертировать:
private fun bitmapToByteArray(context: Context): ByteArray {
    // 1. Получаем Bitmap из ресурсов
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_test)

    // 2. Создаем поток для записи байтов
    val baos = ByteArrayOutputStream()

    // 3. Сжимаем Bitmap в PNG формате (без потерь)
    //    и записываем в поток
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    //      ↑ формат   ↑ качество (0-100)  ↑ куда записывать

    // 4. Преобразуем поток в массив байтов
    return baos.toByteArray()
}

private fun saveBook(
    fireStore: FirebaseFirestore,
    imageUrl: String,
    onComplete: (Boolean) -> Unit
) {
    // Создаем объект книги
    val book = Book(
        title = "Новая книга ${Date().time}",
        author = "Автор",
        description = "Описание книги",
        category = "Художественная литература",
        imageUrl = imageUrl,
        price = 19.99
    )

    fireStore.collection("books")
        .add(book)
        .addOnSuccessListener {
            Log.d("Firestore", "Book added with ID: ${it.id}")
            onComplete(true)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding book", e)
            onComplete(false)
        }
}
