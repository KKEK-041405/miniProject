package com.kkek.chess.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kkek.chess.ChessAppScreen
import com.kkek.chess.model.PieceColor
import com.kkek.chess.model.Square
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel: ViewModel() {
    var isDBConnected: Boolean = false
    var isLogged: Boolean = false
    val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(GameUiState(turn = PieceColor.WHITE))
    private val DB = Firebase.database.reference
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    var PlayerTurn = PieceColor.WHITE
    private val GameID = ""

    fun resetGame() {
        _uiState.value = GameUiState(turn = PieceColor.WHITE)
    }

    fun Login(email: String, password: String, onLoginSuccess: () -> Unit, onLoginFailed: () -> Unit,context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    isLogged = true
                    ConnectToDB()
                    onLoginSuccess()
                    uiState.value.isSignedIn = true
                }
                else{
                    onLoginFailed()
                    Toast.makeText(context, "Login failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun Logout(onLogoutSuccess: () -> Unit) {
        auth.signOut()
        isLogged = false
        onLogoutSuccess()
    }

    fun SignUp(email: String, password: String,onSignUpSuccess: () -> Unit,onSignUpFailed: () -> Unit,context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Login(email, password, onSignUpSuccess, onSignUpFailed,context)
                }
                else{
                    onSignUpFailed()
                    Toast.makeText(context, "Sign up failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun UpdateMoveINDB(value: Square, square: Square) {
        DB.child(GameID).child("Move").child("${value.row}${value.col}").setValue("${square.row}${square.col}")
    }

    fun ConnectToDB() {
       DB.child("Game").child("Player1").get().addOnSuccessListener {
           if (it.value == "") {
               DB.child("Game").child("Player1").setValue(auth.currentUser?.uid.toString())
               PlayerTurn = PieceColor.WHITE
               isDBConnected = true
           }
           else {
               DB.child("Game").child("Player2").get().addOnSuccessListener {
                   if (it.value == "") {
                       DB.child("Game").child("Player2").setValue(auth.currentUser?.uid.toString())
                       PlayerTurn = PieceColor.BLACK
                       isDBConnected = true
                   }
               }
           }
       }
    }
}