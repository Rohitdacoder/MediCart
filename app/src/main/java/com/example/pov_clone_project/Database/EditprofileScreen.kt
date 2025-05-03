package com.example.pov_clone_project.Database

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

@Composable
fun EditProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    if (viewModel.loading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Edit Profile", fontSize = 24.sp, modifier = Modifier.padding(bottom = 20.dp))

            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                viewModel.updateUserProfile()
                navController.popBackStack() // go back to ProfileScreen
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Save")
            }
        }
    }
}