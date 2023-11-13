package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.RangeInterval;
import com.gookjiii.chessapi.board_manager.Spot;

public class Rook extends ChessPiece {
    public Rook(boolean white) {
        super(white);
        super.setChessType("Rook");
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        try {
            if(end.getPiece() != null) {
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

            if(start.getX() == end.getX()) {
                //moving vertically
                RangeInterval getRangeVertical = Board.getLowAndHigh(board.getHashColumn(), start.getX(), start.getY());
                if(start.getY() > end.getY()) {
                    if (getRangeVertical.getA() > end.getY())
                        throw new Exception("Invalid move - obstruction in path.");
                } else {
                    if (getRangeVertical.getB() < end.getY())
                        throw new Exception("Invalid move - obstruction in path.");
                }
            } else if(start.getY() == end.getY()) {
                //moving horizontally
                RangeInterval getRangeHorizontal = Board.getLowAndHigh(board.getHashRow(), start.getY(), start.getX());
                if(start.getX() > end.getX()) {
                    if (getRangeHorizontal.getA() > end.getX())
                        throw new Exception("Invalid move - obstruction in path.");
                } else {
                    if (getRangeHorizontal.getB() < end.getX())
                        throw new Exception("Invalid move - obstruction in path.");
                }
            } else if (start.getY() != end.getY() || start.getX() != end.getX()) {
                throw new Exception("Invalid move - rook can only move vertically or horizontally.");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        if(isWhite()) {
            return "R";
        } else {
            return "r";
        }
    }
}
