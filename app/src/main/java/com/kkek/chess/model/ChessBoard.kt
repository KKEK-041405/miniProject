package com.kkek.chess.model

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt

class Chessboard {
    val board: Array<Array<Square>> = Array(8) { row ->
        Array(8) { col -> Square(row, col) }
    }

    var currentPlayer: PieceColor = PieceColor.WHITE

    init {
        for (i in 0..7) {
            board[1][i].piece = Piece(PieceType.PAWN, PieceColor.BLACK)
            board[6][i].piece = Piece(PieceType.PAWN, PieceColor.WHITE)
        }
        board[0][0].piece = Piece(PieceType.ROOK, PieceColor.BLACK)
        board[0][7].piece = Piece(PieceType.ROOK, PieceColor.BLACK)
        board[7][0].piece = Piece(PieceType.ROOK, PieceColor.WHITE)
        board[7][7].piece = Piece(PieceType.ROOK, PieceColor.WHITE)
        board[0][1].piece = Piece(PieceType.KNIGHT, PieceColor.BLACK)
        board[0][6].piece = Piece(PieceType.KNIGHT, PieceColor.BLACK)
        board[7][1].piece = Piece(PieceType.KNIGHT, PieceColor.WHITE)
        board[7][6].piece = Piece(PieceType.KNIGHT, PieceColor.WHITE)
        board[0][2].piece = Piece(PieceType.BISHOP, PieceColor.BLACK)
        board[0][5].piece = Piece(PieceType.BISHOP, PieceColor.BLACK)
        board[7][2].piece = Piece(PieceType.BISHOP, PieceColor.WHITE)
        board[7][5].piece = Piece(PieceType.BISHOP, PieceColor.WHITE)
        board[0][3].piece = Piece(PieceType.QUEEN, PieceColor.BLACK)
        board[7][3].piece = Piece(PieceType.QUEEN, PieceColor.WHITE)
        board[0][4].piece = Piece(PieceType.KING, PieceColor.BLACK)
        board[7][4].piece = Piece(PieceType.KING, PieceColor.WHITE)
    }


    fun isValidMove(start: Square, end: Square, currentPlayer: PieceColor, pieceType: PieceType = start.piece!!.type): Boolean {
        if (start.piece == null || start.piece!!.color != currentPlayer) return false // No piece or not your turn

        // 1. Check if the end square is within bounds of the board.
        if (end.row !in 0..7 || end.col !in 0..7) return false

        // 2. Check if the end square is occupied by your own piece.
        if (end.piece?.color == currentPlayer) return false

        // 3. Check if the piece type can make the move (e.g., pawn moves, knight's L-shape, etc.)
        when (pieceType) {
            PieceType.PAWN -> {
                if (currentPlayer == PieceColor.WHITE) {
                    if (end.row == start.row - 1 && end.col == start.col && end.piece == null) {
                        return true
                    } else if (start.row == 6 && end.row == start.row - 2 && end.col == start.col &&
                        board[start.row - 1][start.col].piece == null && end.piece == null
                    ) {
                        return true
                    } else if (end.row == start.row - 1 && (end.col == start.col + 1 || end.col == start.col - 1) &&
                        end.piece?.color == PieceColor.BLACK
                    ) {
                        return true
                    }
                } else {
                    if (end.row == start.row + 1 && end.col == start.col && end.piece == null) {
                        return true
                    } else if (start.row == 1 && end.row == start.row + 2 && end.col == start.col &&
                        board[start.row + 1][start.col].piece == null && end.piece == null
                    ) {
                        return true
                    } else if (end.row == start.row + 1 && (end.col == start.col + 1 || end.col == start.col - 1) &&
                        end.piece?.color == PieceColor.WHITE
                    ) {
                        return true
                    }
                }
            }

            PieceType.ROOK -> {
                Log.d("Rook", "${start.row},${start.col}, ${end.row},${end.col}")
                if (start.row == end.row) {
                    // Horizontal move
                    val step = 1
                    if(start.col < end.col){
                        for (col in start.col + step until end.col) {
                            Log.d("Chessboard", "Row: ${start.row},Col: ${col}, ${board[start.row][col].piece}")
                            if (board[start.row][col].piece != null) return false
                        }
                    }
                    else{
                        for (col in start.col - step downTo end.col + step) {
                            Log.d("Chessboard", "Row: ${start.row},Col: ${col}, ${board[start.row][col].piece}")
                            if (board[start.row][col].piece != null) return false
                        }
                    }
                    return true
                } else if (start.col == end.col) {
                    // Vertical move
                    val step = 1
                    if (start.row < end.row){
                        for (row in start.row + step until end.row) {
                            Log.d("Chessboard", "Row: $row,Col: ${start.col}, ${board[row][start.col].piece}")
                            if (board[row][start.col].piece != null) return false
                        }
                    }
                     
                    else{
                        for (row in start.row - step downTo end.row + step){
                            Log.d("Chessboard", "Row: $row,Col: ${start.col}, ${board[row][start.col].piece}")
                            if (board[row][start.col].piece != null) return false
                        }
                    }
                    return true
                }
            }

            PieceType.KNIGHT -> {
                val rowDiff = kotlin.math.abs(start.row - end.row)
                val colDiff = kotlin.math.abs(start.col - end.col)
                return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
            }

            PieceType.BISHOP -> {
                if (kotlin.math.abs(start.row - end.row) == kotlin.math.abs(start.col - end.col)) {
                    val rowStep = if (end.row > start.row) 1 else -1
                    val colStep = if (end.col > start.col) 1 else -1
                    var row = start.row + rowStep
                    var col = start.col + colStep

                    while (row != end.row && col != end.col) {
                        if (board[row][col].piece != null) return false
                        row += rowStep
                        col += colStep
                    }
                    return true
                }
            }

            PieceType.QUEEN -> {
                // Queen moves like a rook or a bishop
                return isValidMove(start, end, currentPlayer, PieceType.ROOK) ||
                        isValidMove(start, end, currentPlayer, PieceType.BISHOP)
            }

            PieceType.KING -> {
                return kotlin.math.abs(start.row - end.row) <= 1 && kotlin.math.abs(start.col - end.col) <= 1
            }
        }
        return false
    }

    fun movePiece(start: Square, end: Square): Boolean {
        Log.d("Chessboard", "Moving piece from ${start.row},${start.col} to ${end.row},${end.col}")
        Log.d("Chessboard", "Current player: $currentPlayer")
        Log.d("ChessBoard","${isValidMove(start,end,currentPlayer)}")
        if (isCheckAfterMove(start, end, currentPlayer)) {
            end.piece = start.piece // Move piece to new square
            start.piece = null // Remove piece from the starting square
            currentPlayer = if (currentPlayer == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
            Log.d("Chessboard", "Piece moved from ${start.row},${start.col} to ${end.row},${end.col}")
            return true
        // ... (you may need additional logic here for special moves like pawn promotion)
        } else {
            return false
            // Handle invalid moves
        }
    }

    fun isCheckAfterMove(start: Square, end: Square, currentPlayer: PieceColor): Boolean {
        if (!isValidMove(start,end,currentPlayer)) return false
        val originalEndPiece = end.piece
        end.piece = start.piece
        start.piece = null

        val isMoveValid = if (isCheck(currentPlayer)) {
            false // Move would put own king in check
        } else {
            true  // Move is valid
        }

        // Undo the simulated move:
        start.piece = end.piece
        end.piece = originalEndPiece

        return isMoveValid
    }

    // ... These functions require more complex logic and are left as placeholders for now.
    fun isCheck(player: PieceColor): Boolean {
        val kingSquare = findKingSquare(player)

        for (row in 0..7) {
            for (col in 0..7) {
                val square = board[row][col]
                if (square.piece != null && square.piece!!.color != player) {
                    if (isValidMove(square, kingSquare, square.piece!!.color)) {
                        return true // King is in check
                    }
                }
            }
        }
        return false
    }

    fun isCheckmate(player: PieceColor): Boolean {
        if (!isCheck(player)) return false // Not in check, so not checkmate

        for (row in 0..7) {
            for (col in 0..7) {
                val startSquare = board[row][col]
                if (startSquare.piece != null && startSquare.piece!!.color == player) {
                    for (endRow in 0..7) {
                        for (endCol in 0..7) {
                            val endSquare = board[endRow][endCol]
                            if (isValidMove(startSquare, endSquare, player)) {
                                // Simulate the move
                                val originalEndPiece = endSquare.piece
                                endSquare.piece = startSquare.piece
                                startSquare.piece = null

                                val stillInCheck = isCheck(player)

                                // Undo the move
                                startSquare.piece = endSquare.piece
                                endSquare.piece = originalEndPiece

                                if (!stillInCheck) {
                                    return false // A move gets the king out of check
                                }
                            }
                        }
                    }
                }
            }
        }
        return true // No valid moves to get out of check
    }

    fun isStalemate(player: PieceColor): Boolean {
        if (isCheck(player)) return false // In check, so not stalemate

        for (row in 0..7) {
            for (col in 0..7) {
                val startSquare = board[row][col]
                if (startSquare.piece != null && startSquare.piece!!.color == player) {
                    for (endRow in 0..7) {
                        for (endCol in 0..7) {
                            val endSquare = board[endRow][endCol]
                            if (isValidMove(startSquare, endSquare, player)) {
                                return false // There is at least one valid move
                            }
                        }
                    }
                }
            }
        }
        return true // No valid moves found
    }

    // Helper function to find the king's square
    private fun findKingSquare(player: PieceColor): Square {
        for (row in 0..7) {
            for (col in 0..7) {
                val square = board[row][col]
                if (square.piece?.type == PieceType.KING && square.piece!!.color == player) {
                    return square
                }
            }
        }
        throw IllegalStateException("King not found on the board!") // This should ideally never happen
    }
}
