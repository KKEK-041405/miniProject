package com.kkek.chess.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.kkek.chess.R

open class ChessPiece{
    open var x: Int = 0
    open var y: Int = 0
    open val drawable: Int = 0
    open val isAlive: Boolean = false
    open val color: Color = Color.White
    open val type: PiecesType = PiecesType.None
}

class None(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece() {
    @DrawableRes
    override val drawable: Int = R.drawable.ic_launcher_background
    override var isAlive: Boolean = false
    override val type: PiecesType = PiecesType.None
}

class Pawn(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if(color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.Pawn
}

class Rook(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if(color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.Rook
}

class Knight(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if (color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.Knight
}

class Bishop(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if (color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.Bishop
}

class Queen(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if (color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.Queen
}

class King(
    override var x: Int,
    override var y: Int, override val color: Color
): ChessPiece(){
    @DrawableRes
    override val drawable: Int = if (color == Color.White) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground
    override var isAlive: Boolean = true
    override val type: PiecesType = PiecesType.King
}
