package com.kkek.chess.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.kkek.chess.ui.GameViewModel

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
