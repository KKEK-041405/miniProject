package com.kkek.chess

//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kkek.chess.ui.screens.GameScreen
import com.kkek.chess.ui.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kkek.chess.ui.GameMode
import com.kkek.chess.ui.screens.HomeScreen
import com.kkek.chess.ui.screens.LoginScreen
import com.kkek.chess.ui.screens.SignUpScreen
import com.kkek.chess.ui.theme.ChessTheme

enum class ChessAppScreen {
    Login,
    SignUp,
    Home,
    Game
}

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        setContent {
            ChessTheme {
                ChessApp()
            }
        }
    }
}

@Composable
fun ChessApp(
    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    Scaffold {
        innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        val startDestination = if (viewModel.auth.currentUser != null) ChessAppScreen.Game.name else ChessAppScreen.Login.name
        val currentScreen = navController.currentBackStackEntry?.destination?.route
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ChessAppScreen.Login.name) {
                LoginScreen(
                    onLoginSuccess = {
                                     navController.navigate(ChessAppScreen.Home.name)
                    },
                    onLoginFailed = {

                    },
                    onSignUp = {
                               navController.navigate(ChessAppScreen.Login.name)
                    },
                    viewModel = viewModel
                )
            }
            composable(route = ChessAppScreen.Home.name){
                //Home Screen
                HomeScreen(
                    gameViewModel = viewModel,
                    onGameStart = {
                        navController.navigate(ChessAppScreen.Game.name)
                    })
            }
            composable(route = ChessAppScreen.Game.name) {
                GameScreen(
                    navController,
                    viewModel
                )
            }
            composable(route = ChessAppScreen.SignUp.name) {
                SignUpScreen(
                    onSignUpSuccess = {
                                      navController.navigate(ChessAppScreen.Home.name)
                    },
                    onSignUpFailed = {},
                    onLogin = {
                              navController.navigate(ChessAppScreen.Login.name)
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}









