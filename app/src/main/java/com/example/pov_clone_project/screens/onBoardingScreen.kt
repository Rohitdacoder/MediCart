import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pov_clone_project.R
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun OnboardingScreen(navController: NavController) {
    val context = LocalContext.current
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("596824304672-gr5ienj3ibrf32m888i748gvuajtjcde.apps.googleusercontent.com") // Add your web client ID here
        .requestEmail()
        .build()

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Handle Google Sign-In result
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (accountTask.isSuccessful) {
                val googleAccount = accountTask.result
                val credential = GoogleAuthProvider.getCredential(googleAccount?.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            navController.navigate("home")  // Navigate to home screen
                        } else {
                            Log.e("GoogleSignIn", "Firebase Authentication failed: ${authTask.exception}")
                        }
                    }
            } else {
                Log.e("GoogleSignIn", "Google Sign-In failed: ${accountTask.exception}")
            }
        }
    )

    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.medicartlogo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Welcome to MediCart",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate("signup") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign up with Email")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { signInWithGoogle() },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign up with Google", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Login")
                    }
                }
            )
        }
    }
}
