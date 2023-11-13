package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.Spot;

public class Knight extends ChessPiece {
	 private final int[] movesX = new int[] {-1, -2, -2, -1, +1, +2, +2, +1};
	 private final int[] movesY = new int[] {+2, +1, -1, -2, -2, -1, +1, +2};

	 public Knight (boolean white) {
		  super(white);
		 super.setChessType("Knight");
	 }

	 @Override public boolean canMove (Board board, Spot start, Spot end) {
		 try {
			 if (checkKingInCheck && board.getKing(isWhite()) != null && board.getKing(isWhite()).inCheck(board)) {
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

			 if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
				 throw new Exception("Cannot move to a cell with a piece of the same color.");
			 }

			 for (int i = 0; i < 8; i++) {
				 if (start.getX() + movesX[i] == end.getX() && start.getY() + movesY[i] == end.getY()) {
					 return true;
				 }
			 }

			 throw new Exception("Invalid move - knight can only move by L letter.");
		 } catch (Exception e) {
			 System.out.println("Invalid move: " + e.getMessage());
			 return false;
		 }
	 }

	 public String toString () {
		  if(isWhite()) {
		  	 return "N";
		  } else {
		  	 return "n";
		  }
	 }
}
