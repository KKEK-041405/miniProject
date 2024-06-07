package com.kkek.chess.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kkek.chess.model.Chessboard
import com.kkek.chess.model.PieceColor
import com.kkek.chess.model.Square
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.annotation.meta.When

class GameViewModel: ViewModel() {
    var isDBConnected: Boolean = false
    var isLogged: Boolean = false
    val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(GameUiState(turn = PieceColor.WHITE))
    private val DB = Firebase.database.reference
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    var PlayerTurn = PieceColor.WHITE
    private val GameID = "Game"

    fun resetGame() {
        _uiState.value = GameUiState(turn = PieceColor.WHITE)
    }

    fun Login(email: String, password: String, onLoginSuccess: () -> Unit, onLoginFailed: () -> Unit,context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    isLogged = true
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
        DB.child(GameID).child("Moves").child("${value.row}${value.col}").setValue("${square.row}${square.col}")
    }
    fun GetMovesFromDB() {
        DB.child(GameID).child("Moves").addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val to = snapshot.value.toString()
                    val row = to[0].digitToInt()
                    val col = to[1].digitToInt()
                    val square = uiState.value.Game.board[row][col]
                    val from = snapshot.key.toString()
                    val fromRow = from[0].digitToInt()
                    val fromCol = from[1].digitToInt()
                    val fromSquare = uiState.value.Game.board[fromRow][fromCol]
                    uiState.value.Game.movePiece(fromSquare,square)
                    Log.d("Snapshot", snapshot.value.toString())
                    Log.d("PreviousChildName", previousChildName.toString())
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }


            }
        )
    }

    fun ConnectToDB(
        Player1: String,
        Player2: String
    ) {
        DB.child(GameID).child("Player1").setValue(Player1)
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
        GetMovesFromDB()
    }

    fun CreateGame(
        Player1: String = "",
        Player2: String = "",
        SelectedGameMode: GameMode,
        onGameCreated: () -> Unit

    ){
        when(SelectedGameMode){
            GameMode.Online ->{

            }
            GameMode.Offline -> {
                uiState.value.Game = Chessboard()
                onGameCreated()
            }
        }
    }
}