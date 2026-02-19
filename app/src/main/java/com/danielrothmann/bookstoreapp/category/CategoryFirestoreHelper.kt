package com.danielrothmann.bookstoreapp.category

import android.content.Context
import android.widget.Toast
import com.danielrothmann.bookstoreapp.data.Category
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object CategoryFirestoreHelper {

    // Добавить категорию
    fun FirebaseFirestore.addCategory(
        category: Category,
        context: Context,
        onSuccess: (String) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val categoryData = hashMapOf(
            "name" to category.name,
            "description" to category.description,
            "bookCount" to 0,
            "isActive" to true
        )

        this.collection("categories")
            .add(categoryData)
            .addOnSuccessListener { documentReference ->
                val categoryId = documentReference.id
                onSuccess(categoryId)
                Toast.makeText(
                    context,
                    "Категория успешно добавлена!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                val error = "Ошибка добавления категории: ${exception.message}"
                onFailure(error)
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
    }

    // Получить все категории
    fun FirebaseFirestore.getAllCategories(
        onSuccess: (List<Category>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        this.collection("categories")
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val categories = result.documents.mapNotNull { document ->
                    Category(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        bookCount = document.getLong("bookCount")?.toInt() ?: 0,
                        isActive = document.getBoolean("isActive") ?: true
                    )
                }
                onSuccess(categories)
            }
            .addOnFailureListener { exception ->
                val error = "Ошибка загрузки категорий: ${exception.message}"
                onFailure(error)
            }
    }

    // Обновить категорию
    fun FirebaseFirestore.updateCategory(
        categoryId: String,
        name: String,
        description: String,
        context: Context,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val updates = hashMapOf<String, Any>(
            "name" to name,
            "description" to description
        )

        this.collection("categories")
            .document(categoryId)
            .update(updates)
            .addOnSuccessListener {
                onSuccess()
                Toast.makeText(
                    context,
                    "Категория обновлена",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->
                val error = "Ошибка обновления: ${exception.message}"
                onFailure(error)
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
    }

    // Удалить категорию (мягкое удаление или полное)
    fun FirebaseFirestore.deleteCategory(
        categoryId: String,
        context: Context,
        hardDelete: Boolean = false,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        if (hardDelete) {
            // Полное удаление из Firestore
            this.collection("categories")
                .document(categoryId)
                .delete()
                .addOnSuccessListener {
                    onSuccess()
                    Toast.makeText(
                        context,
                        "Категория удалена",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    val error = "Ошибка удаления: ${exception.message}"
                    onFailure(error)
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
        } else {
            // Мягкое удаление (просто деактивируем)
            this.collection("categories")
                .document(categoryId)
                .update("isActive", false)
                .addOnSuccessListener {
                    onSuccess()
                    Toast.makeText(
                        context,
                        "Категория деактивирована",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { exception ->
                    val error = "Ошибка деактивации: ${exception.message}"
                    onFailure(error)
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
        }
    }

    // Обновить счетчик книг в категории
    fun FirebaseFirestore.updateCategoryBookCount(
        categoryName: String,
        increment: Int = 1
    ) {
        this.collection("categories")
            .whereEqualTo("name", categoryName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val currentCount = document.getLong("bookCount")?.toInt() ?: 0
                    document.reference.update("bookCount", currentCount + increment)
                }
            }
    }
}
