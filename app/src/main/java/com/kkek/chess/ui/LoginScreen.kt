package com.kkek.chess.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController
import com.kkek.chess.ChessAppScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit, onSignUpFailed: () -> Unit,onLogin: () -> Unit, viewModel: GameViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signUpError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.SignUp(
                    email = email,
                    password = password,
                    onSignUpSuccess = onSignUpSuccess,
                    onSignUpFailed = {
                        onSignUpFailed()
                        signUpError = true
                    },
                    context = context
                )
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Sign Up")
        }
        if (signUpError) {
            Text("Sign up failed. Please try again.", color = MaterialTheme.colorScheme.error)
            signUpError = false // Reset error state
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onLogin ) {
            Text("Already have an account? Log in")
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onLoginFailed: () -> Unit, onSignUp: () -> Unit, viewModel: GameViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.Login(
                    email = email,
                    password = password,
                    onLoginSuccess = onLoginSuccess,
                    onLoginFailed = {
                        onLoginFailed()
                        loginError = true
                    },
                    context = context
                )
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Log in")
        }
        if (loginError) {
            Text("Login failed. Please try again.", color = MaterialTheme.colorScheme.error)
            loginError = false // Reset error state
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onSignUp) {
            Text("Don't have an account? Sign Up")
        }
    }
}