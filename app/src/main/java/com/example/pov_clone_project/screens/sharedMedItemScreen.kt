package com.example.pov_clone_project.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.pov_clone_project.screens.Medicine

class MedicineViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Medicine>>(emptyList())
    val cartItems: StateFlow<List<Medicine>> = _cartItems

    fun addToCart(medicine: Medicine) {
        _cartItems.value = _cartItems.value + medicine
    }
}

