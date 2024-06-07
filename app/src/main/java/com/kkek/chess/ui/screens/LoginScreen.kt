package com.kkek.chess.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.kkek.chess.ui.GameViewModel


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
        EmailLoginSection(
            email = email,
            password = password,
            onEmailChange = { email = it},
            onPasswordChange = { password = it }
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

@Composable
fun EmailLoginSection(email: String, password: String, onEmailChange: (String) -> Unit, onPasswordChange: (String) -> Unit) {
    Column {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password
            modifier = Modifier.fillMaxWidth()
        )
    }
}