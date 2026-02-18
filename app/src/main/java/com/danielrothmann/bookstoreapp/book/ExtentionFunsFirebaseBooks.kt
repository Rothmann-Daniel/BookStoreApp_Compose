package com.danielrothmann.bookstoreapp.book

import android.content.Context
import android.widget.Toast
import com.danielrothmann.bookstoreapp.data.Book
import com.google.firebase.firestore.FirebaseFirestore

// Extension функция для добавления книги
fun FirebaseFirestore.addBook(
    book: Book,
    context: Context,
    onSuccess: (String) -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.collection("books")
        .add(book)
        .addOnSuccessListener { documentReference ->
            val bookId = documentReference.id
            onSuccess(bookId)
            Toast.makeText(
                context,
                "Книга успешно добавлена!",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { exception ->
            val error = "Ошибка добавления книги: ${exception.message}"
            onFailure(error)
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
}

// Extension функция для получения всех книг
fun FirebaseFirestore.getAllBooks(
    onSuccess: (List<Book>) -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.collection("books")
        .get()
        .addOnSuccessListener { result ->
            val books = result.documents.mapNotNull { document ->
                document.toObject(Book::class.java)?.copy(id = document.id)
            }
            onSuccess(books)
        }
        .addOnFailureListener { exception ->
            val error = "Ошибка загрузки книг: ${exception.message}"
            onFailure(error)
        }
}

// Extension функция для получения книг по категории
fun FirebaseFirestore.getBooksByCategory(
    category: String,
    onSuccess: (List<Book>) -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.collection("books")
        .whereEqualTo("category", category)
        .get()
        .addOnSuccessListener { result ->
            val books = result.documents.mapNotNull { document ->
                document.toObject(Book::class.java)
            }
            onSuccess(books)
        }
        .addOnFailureListener { exception ->
            val error = "Ошибка загрузки книг: ${exception.message}"
            onFailure(error)
        }
}

// Extension функция для удаления книги
fun FirebaseFirestore.deleteBook(
    bookId: String,
    context: Context,
    onSuccess: () -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    this.collection("books")
        .document(bookId)
        .delete()
        .addOnSuccessListener {
            onSuccess()
            Toast.makeText(
                context,
                "Книга удалена",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { exception ->
            val error = "Ошибка удаления: ${exception.message}"
            onFailure(error)
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
}