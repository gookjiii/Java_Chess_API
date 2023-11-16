package com.gookjiii.chessapi.entities;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.RangeInterval;
import com.gookjiii.chessapi.board_manager.Spot;

import java.util.HashSet;

public class King extends ChessPiece {
    public King(boolean white) {
        super(white);
        super.setChessType("King");
    }

    private final int[] movesX = new int[] {-1, -2, -2, -1, +1, +2, +2, +1};
    private final int[] movesY = new int[] {+2, +1, -1, -2, -2, -1, +1, +2};

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        try {
            if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
                throw new Exception("Cannot move to a cell with a piece of the same color.");
            }

            if (end.getPiece() == null)
                board.addMove(end);
            board.deleteMove(start);

            HashSet<Spot> checkList = new HashSet<>();
            getChecklist(board, end, checkList);

            for (int i = 0; i < 8; i++) {
                Spot spot = board.getSpot(end.getX() + movesX[i],end.getY() + movesY[i]);
                if (spot.getPiece() != null && spot.getPiece().isWhite() != isWhite() && spot.getPiece() instanceof Knight)
                    throw new Exception("Invalid move - King in check after this move");
            }

            for (int i = 0; i < 8; i++) {
                Spot spot = board.getSpot(end.getX() + dirX[i],end.getY() + dirY[i]);
                if (spot.getPiece() != null && spot.getPiece().isWhite() != isWhite() && spot.getPiece() instanceof King
                        || spot.getPiece() instanceof MegaPawn)
                    throw new Exception("Invalid move - King in check after this move");
            }


            Spot spot_1;
            Spot spot_2;
            if (isWhite()) {
                spot_1 = board.getSpot(end.getX() + 1, end.getY() + 1);
                spot_2 = board.getSpot(end.getX() - 1, end.getY() + 1);
            } else {
                spot_1 = board.getSpot(end.getX() + 1, end.getY() - 1);
                spot_2 = board.getSpot(end.getX() - 1, end.getY() - 1);
            }
            if (spot_1.getPiece() != null && spot_1.getPiece().isWhite() != isWhite() && (spot_1.getPiece()
                    instanceof Pawn) || (spot_2.getPiece() != null && spot_2.getPiece().isWhite() != isWhite() &&
                    (spot_2.getPiece() instanceof Pawn)))
                throw new Exception("Invalid move - King in check after this move");

            for (Spot spot : checkList) {
                if (spot.getPiece() == null) continue;
                if (spot.getPiece() != null && spot.getPiece().isWhite() != isWhite()) {
                    if (spot.getPiece() instanceof Queen) {
                        board.addMove(start);
                        if (end.getPiece() == null)
                            board.deleteMove(end);
                        throw new Exception("Invalid move - King in check after this move");
                    } else if (spot.getPiece() instanceof Rook) {
                        if (end.getX() == spot.getX() || end.getY() == spot.getY()) {
                            board.addMove(start);
                            if (end.getPiece() == null)
                                board.deleteMove(end);
                            throw new Exception("Invalid move - King in check after this move");
                        }
                    } else if (spot.getPiece() instanceof Bishop) {
                        if (Math.abs(end.getX() - spot.getX()) == Math.abs(end.getY() - spot.getY())) {
                            board.addMove(start);
                            if (end.getPiece() == null)
                                board.deleteMove(end);
                            throw new Exception("Invalid move - King in check after this move");
                        }
                    } //if any piece on the board can kill the king after it has made its move, you will be unable to make the move.
                }
            }

            Board.addElement(board.getHashColumn(), start.getX(), start.getY());
            Board.addElement(board.getHashRow(), start.getY(), start.getX());
            Board.addElement(board.getHashDiagonalPositive(), start.getX() - start.getY(), start.getX());
            Board.addElement(board.getHashDiagonalNegative(), start.getX() + start.getY(), start.getX());

            if (start.getX() == end.getX()) {
                return Math.abs(start.getY() - end.getY()) <= 1;
            } else if (start.getY() == end.getY()) {
                return Math.abs(start.getX() - end.getX()) <= 1;
            } else if (Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()) > 2) {
                throw new Exception("Invalid move - King only can move by rule");
            }

            return true;
        } catch (Exception e){
            System.out.println("Invalid move: " + e.getMessage());
            return false;
        }
    }

    private static void getChecklist(Board board, Spot end, HashSet<Spot> checkList) {
        RangeInterval getRangeVertical = Board.getLowAndHigh(board.getHashColumn(), end.getX(), end.getY());
        checkList.add(board.getSpot(end.getX(), getRangeVertical.getA()));
        checkList.add(board.getSpot(end.getX(), getRangeVertical.getB()));
        RangeInterval getRangeHorizontal = Board.getLowAndHigh(board.getHashRow(), end.getY(), end.getX());
        checkList.add(board.getSpot(getRangeHorizontal.getA(), end.getY()));
        checkList.add(board.getSpot(getRangeHorizontal.getB(), end.getY()));
        RangeInterval getRangePositive = Board.getLowAndHigh(board.getHashDiagonalPositive(), end.getX() - end.getY(), end.getX());
        checkList.add(board.getSpot(getRangePositive.getA(),getRangePositive.getA() - (end.getX() - end.getY())));
        checkList.add(board.getSpot(getRangePositive.getB(),getRangePositive.getB() - (end.getX() - end.getY())));
        RangeInterval getRangeNegative = Board.getLowAndHigh(board.getHashDiagonalNegative(), end.getX() + end.getY(), end.getX());
        checkList.add(board.getSpot(getRangeNegative.getA(),(end.getX() + end.getY()) - getRangeNegative.getA()));
        checkList.add(board.getSpot(getRangeNegative.getB(), getRangeNegative.getB() - (end.getX() + end.getY())));
    }

    public boolean inCheck(Board board) {
        HashSet<Spot> checkList = new HashSet<>();
        Spot end = getSpot(board);
        return checkArround(board, checkList, end);
    }

    private boolean checkArround(Board board, HashSet<Spot> checkList, Spot end) {
        getChecklist(board, end, checkList);
        for (int i = 0; i < 8; i++) {
            Spot spot = board.getSpot(end.getX() + movesX[i],end.getY() + movesY[i]);
            if (spot.getPiece() != null && spot.getPiece().isWhite() != isWhite() && spot.getPiece() instanceof Knight)
                return true;
        }

        Spot spot_1;
        Spot spot_2;
        if (isWhite()) {
            spot_1 = board.getSpot(end.getX() + 1, end.getY() + 1);
            spot_2 = board.getSpot(end.getX() - 1, end.getY() + 1);
        } else {
            spot_1 = board.getSpot(end.getX() + 1, end.getY() - 1);
            spot_2 = board.getSpot(end.getX() - 1, end.getY() - 1);
        }
        if (spot_1.getPiece() != null && spot_1.getPiece().isWhite() != isWhite() && (spot_1.getPiece() instanceof Pawn) || (spot_2.getPiece() != null && spot_2.getPiece().isWhite() != isWhite() && (spot_2.getPiece() instanceof Pawn)))
            return true;

        for (Spot spot : checkList) {
            if (spot.getPiece() == null) continue;
            if (spot.getPiece() != null && spot.getPiece().isWhite() != isWhite()) {
                if (spot.getPiece() instanceof Queen) {
                    return true;
                } else if (spot.getPiece() instanceof Rook) {
                    if (end.getX() == spot.getX() || end.getY() == spot.getY()) {
                        return true;
                    }
                } else if (spot.getPiece() instanceof Bishop) {
                    if (Math.abs(end.getX() - spot.getX()) == Math.abs(end.getY() - spot.getY())) {
                        return true;
                    }
                } //if any piece on the board can kill the king after it has made its move, you will be unable to make the move.
            }
        }

        return false;
    }

    public boolean inCheck(Board board, Spot end) {
        HashSet<Spot> checkList = new HashSet<>();
        return checkArround(board, checkList, end);
    }

    private final int[] dirX = new int[] {-1, 0, +1, -1, +1, -1, 0, +1};
    private final int[] dirY = new int[] {-1, -1, -1, 0, 0, +1, +1, +1};
    public boolean inCheckmate(Board board) {
        if (this.inCheck(board)) {
            for (int i = 0; i < 8; i++) {
                Spot spot = board.getSpot(this.getSpot(board).getX() + dirX[i], this.getSpot(board).getY() + dirY[i]);
                if (spot.getPiece() == null) {
                    board.addMove(spot);
                    board.deleteMove(this.getSpot(board));
                    if (!inCheck(board, spot)) {
                        board.addMove(this.getSpot(board));
                        board.deleteMove(spot);
                        return false;
                    }

                    board.addMove(this.getSpot(board));
                    board.deleteMove(spot);
                } else if (spot.getPiece().isWhite() != isWhite()) {
                    board.deleteMove(this.getSpot(board));

                    if (!inCheck(board, spot)) {
                        board.addMove(this.getSpot(board));
                        return false;
                    }

                    board.addMove(this.getSpot(board));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (isWhite()) {
            return "K";
        } else {
            return "k";
        }
    }
}
