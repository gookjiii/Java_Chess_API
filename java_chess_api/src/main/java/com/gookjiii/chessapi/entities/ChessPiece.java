package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.Spot;

public abstract class ChessPiece {
    private String chessType;
    private boolean white;
    private boolean moved = false;
    protected boolean checkKingInCheck = true;

    public ChessPiece(boolean white) {
        this.white = white;
    }

    public void setChessType(String chessType) {
        this.chessType = chessType;
    }

    public static ChessPiece createPiece(String type, boolean isWhite) {
        if ("Pawn".equals(type)) {
            return new Pawn(isWhite);
        } else if ("Rook".equals(type)) {
            return new Rook(isWhite);
        } else if ("Bishop".equals(type)) {
            return new Bishop(isWhite);
        } else if ("King".equals(type)) {
            return new King(isWhite);
        } else if ("Queen".equals(type)) {
            return new Queen(isWhite);
        } else if ("Knight".equals(type)) {
            return new Knight(isWhite);
        } else if ("MegaPawn".equals(type)) {
            return new MegaPawn(isWhite);
        }
        return null;
    }

    public boolean isWhite() {
        return white;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end) throws Exception;

    public void setMoved() {
        moved = true;
    }


    public boolean isMoved() {
        return moved;
    }

    public Spot getSpot(Board board) {
        for (Spot spot : board.getBoard()) {
            if (spot.getPiece() == this) {
                return spot;
            }
        }

        return null;
    }

    @Override
    public abstract String toString();

    public void setWhite(boolean isWhite) {
        this.white = isWhite;
    }

    public String getChessType() {
        return this.chessType != null ? this.chessType : "No piece";
    }
}