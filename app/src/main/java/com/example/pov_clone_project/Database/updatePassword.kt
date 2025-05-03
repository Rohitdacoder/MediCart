package com.example.pov_clone_project.Database

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChangePasswordScreen(navController = NavController(this))
        }
    }
}

@Composable
fun ChangePasswordScreen(navController: NavController) {
    // Local state for storing the new password and error messages
    var newPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    val auth = Firebase.auth

    // Function to handle password change after reauthentication
    fun changePassword(newPassword: String, currentPassword: String) {
        val user = auth.currentUser
        if (user != null && currentPassword.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            // Re-authenticate the user
            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Now, update the password after successful reauthentication
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            errorMessage = "Password changed successfully!"
                            auth.signOut()  // Optionally log out the user
                            navController.navigate("login") {
                                popUpTo("change_password") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Error changing password: ${updateTask.exception?.message}"
                        }
                    }
                } else {
                    errorMessage = "Re-authentication failed: ${task.exception?.message}"
                }
            }
        } else {
            errorMessage = "Please enter your current password and a new password."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display error message if any
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Current password input
        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New password input
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Change password button
        Button(
            onClick = {
                if (newPassword.isNotEmpty() && currentPassword.isNotEmpty()) {
                    changePassword(newPassword, currentPassword)
                } else {
                    errorMessage = "Please enter both current and new passwords."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Change Password")
        }
    }
}

