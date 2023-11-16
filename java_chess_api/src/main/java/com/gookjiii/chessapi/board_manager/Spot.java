package com.gookjiii.chessapi.board_manager;

import java.util.Objects;

import com.gookjiii.chessapi.entities.ChessPiece;

/**
 * Represents a spot within a Board.
 */
public class Spot {
    private ChessPiece piece;
    private int x, y;

    public Spot(int x, int y, ChessPiece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
    public ChessPiece getPiece() {
        return piece;
    }
    public void setPiece(ChessPiece piece) {
        this.piece = piece != null ? ChessPiece.createPiece(piece.getChessType(),piece.isWhite()) : null;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return x == spot.x &&
                y == spot.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        if (getPiece() != null) {
            return getPiece().toString();
        } else {
            return null;
        }
    }
}
