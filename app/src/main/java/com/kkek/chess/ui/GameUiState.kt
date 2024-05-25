package com.kkek.chess.ui

import android.text.BoringLayout
import com.kkek.chess.model.Chessboard
import com.kkek.chess.model.PieceColor

data class GameUiState(
    var isGameOver: Boolean = false,
    val Game: Chessboard = Chessboard(),
    var turn: PieceColor,
    var isSignedIn: Boolean = false
)