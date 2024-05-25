package com.kkek.chess.model

data class Piece(
    val type: PieceType,
    val color: PieceColor
)

enum class PieceType { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }
enum class PieceColor { WHITE, BLACK }