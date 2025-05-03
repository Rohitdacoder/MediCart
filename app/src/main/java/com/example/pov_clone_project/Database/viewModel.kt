package com.example.pov_clone_project.Database

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var loading by mutableStateOf(false)

    fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        loading = true

        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    name = document.getString("name") ?: ""
                    email = document.getString("email") ?: ""
                }
                loading = false
            }
            .addOnFailureListener {
                Log.e("Profile", "Failed to fetch: ${it.message}")
                loading = false
            }
    }

    fun updateUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        loading = true
        val userMap = mapOf("name" to name, "email" to email)

        db.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                loading = false
            }
            .addOnFailureListener {
                Log.e("Profile", "Update failed: ${it.message}")
                loading = false
            }
    }

    fun createUserInFirestore(uid: String, name: String, email: String) {
        val db = Firebase.firestore
        val user = mapOf("name" to name, "email" to email)

        db.collection("users").document(uid).set(user)
    }

}
