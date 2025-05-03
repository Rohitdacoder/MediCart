package com.example.pov_clone_project.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pov_clone_project.Database.ThemePreference
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var isDarkMode by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Profile", fontSize = 24.sp, modifier = Modifier.padding(bottom = 20.dp))

        ProfileItem("View / Edit Profile") {
            navController.navigate("edit_profile")
        }
        ProfileItem("Change Password") {
             navController.navigate("change_password")
        }
        ProfileItem("Notification Settings") {
             navController.navigate("notifications")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {  }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("App Theme (Dark/Light)", modifier = Modifier.weight(1f))
            Switch(
                checked = false,
                onCheckedChange = {}
        )
     }

        ProfileItem("Payment Options") {
            navController.navigate("payment_options")
        }
        var showDialog by remember { mutableStateOf(false) }

        ProfileItem("Delete Account") {
            showDialog = true
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to delete: ${task.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                        showDialog = false
                    }) {
                        Text("Yes, Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete your account? This action cannot be undone.") }
            )
        }
        ProfileItem("Log Out") {
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }
}

@Composable
fun ProfileItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 16.sp)
        }
    }
}
