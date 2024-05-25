package com.kkek.chess.model

data class Square(
    val row: Int,
    val col: Int,
    var piece: Piece? = null
)