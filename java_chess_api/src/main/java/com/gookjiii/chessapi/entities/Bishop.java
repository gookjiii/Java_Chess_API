package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.RangeInterval;
import com.gookjiii.chessapi.board_manager.Spot;

public class Bishop extends ChessPiece {
    public Bishop(boolean white) {
        super(white);
        super.setChessType("Bishop");
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) throws Exception {
        try {
            if (end.getPiece() != null) {
                if (end.getPiece().isWhite() == this.isWhite()) {
                    // can't kill piece of same color
                    throw new Exception("Cannot move to a cell with a piece of the same color.");
                }
            } else if (checkKingInCheck && board.getKing(isWhite()) != null && board.getKing(isWhite()).inCheck(board)) {
                throw new Exception("Cannot move while in check.");
            }

            if (checkKingInCheck) {

                if (end.getPiece() == null)
                    board.addMove(end);
                board.deleteMove(start);

                if (board.getKing(isWhite()) != null && board.getKing(isWhite()).inCheck(board)) {
                    board.addMove(start);
                    if (end.getPiece() == null)
                        board.deleteMove(end);
                    throw new Exception("Invalid move - King in check after this move");
                }

                board.addMove(start);
                if (end.getPiece() == null)
                    board.deleteMove(end);
            }

            checkKingInCheck = true;

            if (Math.abs(start.getX() - end.getX()) == Math.abs(start.getY() - end.getY())) {
                RangeInterval getRangePositive = Board.getLowAndHigh(board.getHashDiagonalPositive(), start.getX() - start.getY(), start.getX());
                RangeInterval getRangeNegative = Board.getLowAndHigh(board.getHashDiagonalNegative(), start.getX() + start.getY(), start.getX());

                if ((start.getX() - end.getX()) == (start.getY() - end.getY())) {
                    if (getRangePositive.getA() + 1 > end.getX() && getRangePositive.getB() - 1 < end.getX()) {
                        throw new Exception("Invalid move - obstruction in path.");
                    }
                }

                if ((start.getX() - end.getX()) == -(start.getY() - end.getY())) {
                    if (getRangeNegative.getA() + 1 > end.getX() && getRangeNegative.getB() - 1 < end.getX()) {
                        throw new Exception("Invalid move - obstruction in path.");
                    }
                }

                return true;
            } else {
                throw new Exception("Invalid move - bishop can only move on diagonals.");
            }
        } catch (Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "B";
        } else {
            return "b";
        }
    }
}
