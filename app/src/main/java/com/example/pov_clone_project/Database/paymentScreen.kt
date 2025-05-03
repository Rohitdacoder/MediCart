package com.example.pov_clone_project.Database

// Required imports
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razorpay.Checkout
import org.json.JSONObject

@Composable
fun PaymentOptionsScreen(context: Context) {
    val activity = context as Activity
    val paymentMethods = listOf("UPI", "Card", "Wallet")
    var selectedMethod by remember { mutableStateOf(paymentMethods[0]) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Choose Payment Method")

        paymentMethods.forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedMethod = method }
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedMethod == method,
                    onClick = { selectedMethod = method }
                )
                Text(text = method)
            }
        }

        Button(
            onClick = {
                startRazorPayPayment(activity)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Proceed to Pay")
        }
    }
}

fun startRazorPayPayment(activity: Activity) {
    val checkout = Checkout()
    checkout.setKeyID("YOUR_API_KEY_HERE") // Replace with your Razorpay key

    try {
        val options = JSONObject()
        options.put("name", "Rohit Sharma")
        options.put("description", "Test Payment")
        options.put("currency", "INR")
        options.put("amount", "50000") // Amount in paise (₹500.00)

        val prefill = JSONObject()
        prefill.put("email", "test@example.com")
        prefill.put("contact", "9999999999")

        options.put("prefill", prefill)

        checkout.open(activity, options)
    } catch (e: Exception) {
        Toast.makeText(activity, "Payment Error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}


