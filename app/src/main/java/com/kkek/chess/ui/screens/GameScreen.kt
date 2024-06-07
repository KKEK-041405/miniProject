package com.kkek.chess.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kkek.chess.ChessAppScreen
import com.kkek.chess.R
import com.kkek.chess.model.Piece
import com.kkek.chess.model.PieceColor
import com.kkek.chess.model.PieceType
import com.kkek.chess.model.Square
import com.kkek.chess.ui.GameViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GameScreen(
    navController: NavController,
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val selectedSquare = remember { mutableStateOf<Square?>(null) }
    val game = gameViewModel.uiState.value.Game
    Column(modifier = Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Box(modifier = Modifier){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                for (i in 0..7) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (j in 0..7) {
                            val square = game.board[i][j]
                            val borderColor: Color
                            if (selectedSquare.value == square){
                                borderColor = Color.Green
                            }
                            else if (
                                selectedSquare.value != null &&
                                game.isCheckAfterMove(selectedSquare.value!!,square,game.currentPlayer)){
                                if (square.piece != null){
                                    borderColor = Color.Red
                                }
                                else{
                                    borderColor = Color.Yellow
                                }
                            }
                            else{
                                borderColor = Color.Black
                            }
                            ChessboardSquare(
                                modifier = Modifier,
                                square = square,
                                borderColor = borderColor,
                                isSelected = selectedSquare.value == square, // For highlighting
                                onSquareClick = {
                                    if (gameViewModel.PlayerTurn != game.currentPlayer) return@ChessboardSquare
                                    // Handle square clicks
                                    if ((selectedSquare.value == null && square.piece != null)) {
                                        // If no square selected, select the clicked square
                                        if(square.piece!!.color == game.currentPlayer){
                                            selectedSquare.value = square
                                        }
                                    } else if (selectedSquare.value?.piece != null) {
                                        if (square.piece != null && square.piece!!.color == game.currentPlayer){
                                            selectedSquare.value = square
                                        }
                                        else{
                                            if(game.movePiece(selectedSquare.value!!, square)) {
                                                gameViewModel.uiState.value.turn = game.currentPlayer
                                                gameViewModel.UpdateMoveINDB(selectedSquare.value!!,square)
                                                selectedSquare.value = null // Clear selection after move

                                            }
                                        }
                                        // If a square is already selected, attempt a move
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = "Current Player: ${game.currentPlayer}"
        )
        Text(text = "isCheck: ${game.isCheck(game.currentPlayer)}")
        Text(text = "isCheckMate: ${game.isCheckmate(game.currentPlayer)}")
        Text(text = "isStalemate: ${game.isStalemate(game.currentPlayer)}")
        Button(onClick = { gameViewModel.Logout { navController.navigate(ChessAppScreen.Login.name) } }) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun ChessboardSquare(
    modifier: Modifier = Modifier,
    square: Square,
    isSelected: Boolean,
    onSquareClick: () -> Unit,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onSquareClick() }
            .background(
                if ((square.row + square.col) % 2 == 0) Color.LightGray else Color.DarkGray
            )
            .border(
                width = if (borderColor != Color.Black) 4.dp else 1.dp,
                color = borderColor
            )
    ){
        if (square.piece != null) {
            PieceView(piece = square.piece!!)
        }
    }
}

@Composable
fun PieceView(piece: Piece) {
    val imageResource = when (piece.type) {
        PieceType.PAWN -> if (piece.color == PieceColor.WHITE) R.drawable.pawn_w else R.drawable.pawn_b
        PieceType.ROOK -> if (piece.color == PieceColor.WHITE) R.drawable.rook_w else R.drawable.rook_b
        PieceType.KNIGHT -> if (piece.color == PieceColor.WHITE) R.drawable.knight_w else R.drawable.knight_b
        PieceType.BISHOP -> if (piece.color == PieceColor.WHITE) R.drawable.bishop_w else R.drawable.bishop_b
        PieceType.QUEEN -> if (piece.color == PieceColor.WHITE) R.drawable.queen_w else R.drawable.queen_b
        PieceType.KING -> if (piece.color == PieceColor.WHITE) R.drawable.king_w else R.drawable.king_b
    }
    Image(painterResource(id = imageResource), contentDescription = null)
}