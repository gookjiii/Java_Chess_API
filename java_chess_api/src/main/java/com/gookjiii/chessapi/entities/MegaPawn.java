package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.Spot;

public class MegaPawn extends ChessPiece {
    public MegaPawn(boolean white) {
        super(white);
        super.setChessType("MegaPawn");
    }

    @Override public boolean canMove (Board board, Spot start, Spot end) {
        try {
            boolean thisMoveRemoveCheck = false;
            if (checkKingInCheck && board.getKing(isWhite()) != null && board.getKing(isWhite()).inCheck(board)) {
                Spot tempSpot = end;
                board.remove(end);
                if (board.getKing(isWhite()).inCheck(board))
                    throw new Exception("Cannot move while in check.");
                board.add(tempSpot);
                thisMoveRemoveCheck = true;
            }

            if (checkKingInCheck && !thisMoveRemoveCheck) {
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

            if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
                //Can't kill or move over piece of same color
                throw new Exception("Cannot move to a cell with a piece of the same color.");
            }

            if (end.getPiece() != null && start.getX() == end.getX()) {
                // can't kill piece moving forwards
                throw new Exception("Invalid move - Can't kill piece moving forwards.");
            }

            if (start.getX() == end.getX()) {
                return Math.abs(start.getY() - end.getY()) <= 1;
            } else if (start.getY() == end.getY()) {
                return Math.abs(start.getX() - end.getX()) <= 1;
            } else if (Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()) > 2) {
                throw new Exception("Invalid move - MegaPawn only can move by King rule");
            }

            return false;
        } catch (Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "M";
        } else {
            return "m";
        }
    }
}
