package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.RangeInterval;
import com.gookjiii.chessapi.board_manager.Spot;

public class Queen extends ChessPiece {
	 public Queen (boolean white) {
		  super(white);
		 super.setChessType("Queen");
	 }

	 @Override public boolean canMove (Board board, Spot start, Spot end) throws Exception {
		 try {
			 boolean thisMoveRemoveCheck = false;
			 if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
				 throw new Exception("Cannot move to a cell with a piece of the same color.");
			 } else if (checkKingInCheck && board.getKing(isWhite()) != null && board.getKing(isWhite()).inCheck(board)) {
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

			 RangeInterval getRangeVertical = Board.getLowAndHigh(board.getHashColumn(), start.getX(), start.getY());
			 RangeInterval getRangeHorizontal = Board.getLowAndHigh(board.getHashRow(), start.getY(), start.getX());
			 RangeInterval getRangePositive = Board.getLowAndHigh(board.getHashDiagonalPositive(), start.getX() - start.getY(), start.getX());
			 RangeInterval getRangeNegative = Board.getLowAndHigh(board.getHashDiagonalNegative(), start.getX() + start.getY(), start.getX());

			 if (start.getX() == end.getX()) {
				 if (start.getY() > end.getY()) {
					 if (getRangeVertical.getA() > end.getY())
						 throw new Exception("Invalid move - obstruction in path.");
				 } else {
					 if (getRangeVertical.getB() < end.getY())
						 throw new Exception("Invalid move - obstruction in path.");
				 }
				 return true;
			 } else if (start.getY() == end.getY()) {
				 if (start.getX() > end.getX()) {
					 if (getRangeHorizontal.getA() > end.getX())
						 throw new Exception("Invalid move - obstruction in path.");
				 } else {
					 if (getRangeHorizontal.getB() < end.getX())
						 throw new Exception("Invalid move - obstruction in path.");
				 }
				 return true;
			 } else if (Math.abs(start.getX() - end.getX()) == Math.abs(start.getY() - end.getY())) {
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
				 throw new Exception("Invalid move - queen can only move vertically or horizontally or on diagonals.");
			 }
		 } catch (Exception e) {
			 System.out.println("Invalid move: " + e.getMessage());
			 return false;
		 }
	 }

	 @Override public String toString () {
		  if (isWhite()) {
				return "Q";
		  } else {
				return "q";
		  }
	 }
}
