package com.example.pov_clone_project.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pov_clone_project.R // Add your local images in res/drawable if needed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediCartHomeScreen(navController: NavController) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val selectedItem = when (currentRoute) {
        "home" -> 0
        "medicine" -> 1
        "cart" -> 2
        "profile" -> 3
        else -> 0
    }
    Scaffold(
        topBar = { HomeTopAppBar() },
        bottomBar = {
            BottomNavigationBar(navController, selectedItem) { index ->
                when (index) {
                    0 -> navController.navigate("home")
                    1 -> navController.navigate("medicine")
                    2 -> navController.navigate("cart")
                    3 -> navController.navigate("profile")
                }
            }
        }
    ) { padding ->
        HomeContent(Modifier.padding(padding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        title = { Text("MediCart") },
        actions = {
            IconButton(onClick = { /* Handle Notification */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = { /* Handle Profile */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = {
                if(selectedItem != 0) {
                    navController.navigate("home") // Navigate to home screen
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Medication, contentDescription = "Medicine") },
            label = { Text("Medicine") },
            selected = selectedItem == 1,
            onClick = {
                if(selectedItem != 1) {
                    navController.navigate("medicine") // Navigate to medicine screen
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = selectedItem == 2,
            onClick = {
                if (selectedItem != 2) {
                    navController.navigate("cart") // Navigate to cart screen
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == 3,
            onClick = {
                if (selectedItem != 3) {
                    navController.navigate("profile") // Navigate to profile screen
                }
            }
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar()
        Spacer(modifier = Modifier.height(12.dp))

        BannerSection()

        Spacer(modifier = Modifier.height(20.dp))

        CategoryGrid()

        Spacer(modifier = Modifier.height(20.dp))

        FrequentlyBoughtSection()

        Spacer(modifier = Modifier.height(20.dp))

        ExploreSection()
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        placeholder = { Text("Search here...") },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
            disabledContainerColor = Color(0xFFF0F0F0)
        )
    )
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFE0F7FA), shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.banner), contentDescription = "Banner",contentScale = ContentScale.Crop, modifier = Modifier.fillMaxWidth())
    }
}

data class Category(val name: String, val icon: ImageVector)

val categories = listOf(
    Category("Medicines", Icons.Default.MedicalServices),
    Category("Lab Tests", Icons.Default.Biotech),
    Category("Healthcare", Icons.Default.HealthAndSafety),
    Category("Cosmetic", Icons.Default.Face),
    Category("Chat", Icons.Default.Chat),
    Category("Doctors", Icons.Default.LocalHospital)
)

@Composable
fun CategoryGrid() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.take(3).forEach { category ->
                CategoryItem(category)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.drop(3).forEach { category ->
                CategoryItem(category)
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(category.icon, contentDescription = category.name, modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(category.name, fontSize = 12.sp)
    }
}

@Composable
fun FrequentlyBoughtSection() {
    Text("Frequently Bought", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    val products = listOf("Zerodol-P", "Montair-L", "Monocef-OCV")

    LazyRow {
        items(products) { product ->
            Box(
                modifier = Modifier
                    .size(120.dp, 100.dp)
                    .padding(8.dp)
                    .background(Color(0xFFE1F5FE), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(product)
            }
        }
    }
}

@Composable
fun ExploreSection() {
    Text("Explore as you like", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { /* Offer Zone */ }, shape = RoundedCornerShape(20.dp)) {
            Text("Offer Zone %")
        }
        Button(onClick = { /* Premium */ }, colors = ButtonDefaults.buttonColors(containerColor = Color.Green), shape = RoundedCornerShape(20.dp)) {
            Text("Premium", color = Color.White)
        }
    }
}
