package com.example.pov_clone_project.navigation

import OnboardingScreen
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pov_clone_project.Database.ChangePasswordScreen
import com.example.pov_clone_project.Database.EditProfileScreen
import com.example.pov_clone_project.Database.NotificationSettingsScreen
import com.example.pov_clone_project.Database.PaymentOptionsScreen
import com.example.pov_clone_project.screens.CartScreen
//import com.example.pov_clone_project.screens.CartScreen
import com.example.pov_clone_project.screens.LoginScreen
import com.example.pov_clone_project.screens.MediCartHomeScreen
import com.example.pov_clone_project.screens.Medicine
import com.example.pov_clone_project.screens.MedicineScreen
import com.example.pov_clone_project.screens.MedicineViewModel
import com.example.pov_clone_project.screens.ProfileScreen
import com.example.pov_clone_project.screens.SignupScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pov_clone_project.screens.MapScreen


@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val cartItems = remember { mutableStateListOf<Medicine>() }
    val medicineViewModel: MedicineViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            MediCartHomeScreen(navController)
        }
        composable("medicine") {
            MedicineScreen(navController, medicineViewModel)
        }
        /*composable("medicine") {
            MedicineScreen(
                navController = navController,
                onAddToCart = { medicine ->
                    cartItems.add(medicine)
                },
                onGoToCart = {
                    navController.navigate("cart")
                }
            )
        }*/
        composable("cart") {
            CartScreen(navController, medicineViewModel)
        }
        composable("map") {
            MapScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
        composable("change_password") {
            ChangePasswordScreen(navController)
        }
        composable("notifications") {
            NotificationSettingsScreen(navController)
        }
        composable("payment_options") {
            PaymentOptionsScreen(context = LocalContext.current)
        }
    }
}
