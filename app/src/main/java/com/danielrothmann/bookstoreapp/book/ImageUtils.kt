package com.danielrothmann.bookstoreapp.book

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {

    // Конвертация Uri в Base64 строку
    fun uriToBase64(context: Context, uri: Uri, maxSize: Int = 800): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Изменяем размер для оптимизации
            val resizedBitmap = resizeBitmap(bitmap, maxSize)
            bitmapToBase64(resizedBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Конвертация Bitmap в Base64
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Изменение размера изображения
    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val ratio = width.toFloat() / height.toFloat()

        return if (width > height) {
            val newWidth = maxSize
            val newHeight = (maxSize / ratio).toInt()
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            val newHeight = maxSize
            val newWidth = (maxSize * ratio).toInt()
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        }
    }

    // Конвертация Base64 в Bitmap для отображения
    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}