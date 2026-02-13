package com.danielrothmann.bookstoreapp.profile

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AdminChecker {

    fun isAdmin(
        auth: FirebaseAuth = FirebaseAuth.getInstance(),
        db: FirebaseFirestore = FirebaseFirestore.getInstance(),
        onResult: (Boolean) -> Unit
    ) {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Log.d("admin", "User not authenticated")
            onResult(false)
            return
        }

        db.collection("admin")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                val isAdmin = document.exists()
                Log.d("admin", "User ${currentUser.uid} isAdmin: $isAdmin")
                onResult(isAdmin)
            }
            .addOnFailureListener { exception ->
                Log.e("admin", "Error checking admin status", exception)
                onResult(false)
            }
    }
}