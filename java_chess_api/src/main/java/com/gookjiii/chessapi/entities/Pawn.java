package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.Spot;

public class Pawn extends ChessPiece {
	 public Pawn (boolean white) {
		  super(white);
		 super.setChessType("Pawn");
	 }

	 @Override public boolean canMove (Board board, Spot start, Spot end) throws Exception {
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

			 if (checkKingInCheck && thisMoveRemoveCheck) {
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

			 if (!isMoved() && start.getX() == end.getX() && Math.abs(end.getY() - start.getY()) <= 2) {
				 if ((end.getY() > start.getY() && isWhite()) || (end.getY() < start.getY() && !isWhite())) {
					 return true;
				 }
			 } else if (start.getX() == end.getX() && Math.abs(start.getY() - end.getY()) < 2) {
				 if ((end.getY() > start.getY() && isWhite()) || (end.getY() < start.getY() && !isWhite())) {
					 return true;
				 }
			 } else if (end.getX() == start.getX() - 1 || end.getX() == start.getX() + 1) {
				 if (end.getPiece() != null && end.getPiece().isWhite() != isWhite()) {
					 if ((isWhite() && end.getY() == start.getY() + 1) || (!isWhite() && end.getY() == start.getY() - 1))
						 return true;
				 }
			 }

			 throw new Exception("Invalid move - pawn only can move by rule.");
		 } catch (Exception e) {
			 System.out.println("Invalid move: " + e.getMessage());
			 return false;
		 }
	 }

	 @Override public String toString () {
		  if (isWhite()) {
				return "P";
		  } else {
				return "p";
		  }
	 }
}
