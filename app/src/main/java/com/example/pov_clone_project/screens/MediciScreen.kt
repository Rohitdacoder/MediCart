package com.example.pov_clone_project.screens


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.Serializable
import androidx.compose.foundation.lazy.items
import kotlinx.coroutines.launch
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import java.io.InputStream

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://odlgvqrfmdmmupsjkxvm.supabase.co",  // Replace with your actual URL
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9kbGd2cXJmbWRtbXVwc2preHZtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYwMjA0OTYsImV4cCI6MjA2MTU5NjQ5Nn0.iz2d-AFkU7d96zUz6Kc2Da4CFmFQdLG5fqCHlFnFUxk"  // Replace with your actual Supabase API Key
    ) {
        install(Postgrest)  // For interacting with the database (PostgREST)
    }
}

@Serializable
data class Medicine(
    val name: String,
    val image_url: String,
    val description: String,
    val price : Float
)

@Composable
fun MedicineScreen(navController: NavHostController,viewModel: MedicineViewModel) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var medicineList by remember { mutableStateOf<List<Medicine>>(emptyList()) }


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val result = SupabaseClient.client
                    .postgrest["medinfo"]
                    .select()
                    .decodeList<Medicine>()
                medicineList = result
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to fetch medicines: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val filteredList = medicineList.filter {
        it.name.contains(searchText.text, ignoreCase = true) ||
            it.description.contains(searchText.text, ignoreCase = true)
    }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Toast.makeText(context, "File Selected: ${it.lastPathSegment}", Toast.LENGTH_SHORT).show()
            // You can now upload this file to Supabase Storage or any API
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medicines") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 8.dp
            )
        }
    ) {padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            var showSuggestions by remember { mutableStateOf(true) }
            var selectedMedicine by remember { mutableStateOf<Medicine?>(null) }
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    showSuggestions = true
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search Medicines") },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = showSuggestions && searchText.text.isNotBlank(),
                onDismissRequest = { showSuggestions = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                filteredList.take(5).forEach { medicine ->
                    DropdownMenuItem(onClick = {
                        searchText = TextFieldValue(medicine.name)
                        selectedMedicine = medicine
                        showSuggestions = false
                    }) {
                        Text(medicine.name)
                    }
                }
            }   

            Spacer(modifier = Modifier.height(16.dp))

            selectedMedicine?.let {
                MedicineCard(medicine = it, navController = navController, viewModel = viewModel, showAddToCartButton = true)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Upload Prescription
            Button(
                onClick = { filePickerLauncher.launch("*/*")},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Upload Prescription")
            }
        }
    }
}

/*@Composable
fun MedicineCard(medicine: Medicine,navController:NavHostController,viewModel: MedicineViewModel) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = medicine.image_url,
                    contentDescription = medicine.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = medicine.name,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = medicine.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "₹${medicine.price}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
            /*Icon(
                imageVector = Icons.Default.AddShoppingCart,
                contentDescription = "Add to Cart",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(28.dp).clickable {
                    // Add to cart logic here
                    Toast.makeText(
                        context,
                        "${medicine.name}  added to cart",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            )*/
            Button(
                onClick = {
                    selectedMedicine?.let {
                        viewModel.addToCart(it)
                        navController.navigate("cart") // Navigate to CartScreen
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add to Cart")
            }
        }
    }
}*/
@Composable
fun MedicineCard(
    medicine: Medicine,
    navController: NavController,
    viewModel: MedicineViewModel,
    showAddToCartButton: Boolean
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = medicine.image_url,
                    contentDescription = medicine.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(70.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = medicine.name,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = medicine.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "₹${medicine.price}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            Button(
                onClick = {
                    viewModel.addToCart(medicine)
                    Toast.makeText(
                        context,
                        "${medicine.name} added to cart",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("cart")
                },
                modifier = Modifier
                    .height(40.dp)
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Add")
            }
        }
    }
}

