package com.gookjiii.chessapi.board_manager;


import com.gookjiii.chessapi.entities.*;
import com.gookjiii.chessapi.listener.CheckListener;
import com.gookjiii.chessapi.listener.CheckmateListener;

import java.util.*;

public class Board {
    private HashMap<Integer, TreeSet<Integer>> hashColumn = new HashMap<>();
    private HashMap<Integer, TreeSet<Integer>> hashRow = new HashMap<>();
    private HashMap<Integer, TreeSet<Integer>> hashDiagonalPositive = new HashMap<>();
    private HashMap<Integer, TreeSet<Integer>> hashDiagonalNegative = new HashMap<>();
    private HashSet<Spot> board = new HashSet<>();
    private HashMap<Coordinate, Spot> newBoard = new HashMap<>();

    private List<CheckListener> checkListeners = new ArrayList<>();
    private List<CheckmateListener> checkmateListeners = new ArrayList<>();

    public void addCheckListener(CheckListener listener) {
        checkListeners.add(listener);
    }

    public void addCheckmateListener(CheckmateListener listener) {
        checkmateListeners.add(listener);
    }

    private void notifyCheckListeners(boolean isWhite) {
        for (CheckListener listener : checkListeners) {
            listener.onCheck(isWhite);
        }
    }

    private void notifyCheckmateListeners(boolean isWhite) {
        for (CheckmateListener listener : checkmateListeners) {
            listener.onCheckmate(isWhite);
        }
    }

    private boolean whiteToMove = true;

    private final List<Coordinate> megaPawnCells;

    private King kingW = null;
    private King kingB = null;

    public HashMap<Integer, TreeSet<Integer>> getHashColumn() {
        return hashColumn;
    }

    public HashMap<Integer, TreeSet<Integer>> getHashRow() {
        return hashRow;
    }

    public HashMap<Integer, TreeSet<Integer>> getHashDiagonalPositive() {
        return hashDiagonalPositive;
    }

    public HashMap<Integer, TreeSet<Integer>> getHashDiagonalNegative() {
        return hashDiagonalNegative;
    }

    public static void addElement(HashMap<Integer, TreeSet<Integer>> map, int value, int key) {
        if (!map.containsKey(key)) {
            map.put(key, new TreeSet<>());
        }
        map.get(key).add(value);
    }

    public static void deleteElement(HashMap<Integer, TreeSet<Integer>> map, int value, int key) {
        if (map.containsKey(key)) {
            TreeSet<Integer> values = map.get(key);
            values.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
        }
    }

    public static RangeInterval getLowAndHigh(HashMap<Integer, TreeSet<Integer>> map, int key, int point) {
        if (map.containsKey(key)) {
            TreeSet<Integer> values = map.get(key);
            return new RangeInterval((values.floor(point - 1) != null ? values.floor(point - 1) : -Integer.MAX_VALUE), (values.ceiling(point + 1) != null ? values.ceiling(point + 1) : Integer.MAX_VALUE));
        } else {
            return new RangeInterval(-Integer.MAX_VALUE,Integer.MAX_VALUE);
        }
    }

    public Board(List<Coordinate> megaPawnCells) {
        board.clear();
        whiteToMove = true;
        this.megaPawnCells = megaPawnCells;
    }

    public void putPiece(ChessPiece piece, int x, int y, boolean isWhite) throws Exception {
        try {
            if (Objects.equals(piece.getChessType(), "MegaPawn")) {
                throw new Exception("Cannot add MegaPawn manually.");
            }
            Spot existingSpot = newBoard.get(new Coordinate(x, y));
            if (existingSpot != null) {
                throw new Exception("A piece already exists at the specified coordinates.");
            }

            Spot newSpot = new Spot(x, y, piece);
            newSpot.getPiece().setWhite(isWhite); // Set the piece's color

            if (Objects.equals(piece.getChessType(), "King")) {
                if (isWhite)
                    kingW = (King) piece;
                else kingB = (King) piece;
            }

            // Add the new Spot to the board
            board.add(newSpot);
            newBoard.put(new Coordinate(x, y), newSpot);
            addElement(hashColumn, y, x);
            addElement(hashRow, x, y);
            addElement(hashDiagonalPositive, x, x - y);
            addElement(hashDiagonalNegative, x, x + y);

            System.out.printf("Put %s to [%d,%d]\n", newSpot.getPiece(), newSpot.getX(), newSpot.getY());

            boolean isInCheck = getKing(!isWhite) != null ? getKing(!isWhite).inCheck(this) : false;
            if (isInCheck) {
                notifyCheckListeners(!isWhite);
                boolean isCheckmate = kingInCheckmate();
                if (isCheckmate) {
                    notifyCheckmateListeners(!isWhite);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getFigure(int x, int y) throws Exception {
        try {
            Spot existingSpot = newBoard.get(new Coordinate(x, y));
            if (existingSpot != null) {
                System.out.printf("[%d,%d] %s\n", existingSpot.getX(), existingSpot.getY(), existingSpot.getPiece());
            } else {
                throw new Exception("No figure on this cell");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public Spot getSpot(int x, int y) {
        Spot existingSpot = newBoard.get(new Coordinate(x, y));
        return existingSpot != null ? existingSpot : new Spot(x,y, null);
    }

    public King getKing(boolean white) {
        if (white) {
            if (kingW != null) return (King) kingW;
        } else {
            if (kingB != null) return (King) kingB;
        }
        return null;
    }
    public boolean move(Spot begin, Spot end) throws Exception {
        try {
            if (begin.getPiece() != null)
                return move(begin, end, false);
            else
                throw new Exception("No figure on the cell with the given coordinates");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

//    public boolean moveIgnoreTurn(Spot begin, Spot end) throws Exception {
//        return move(begin, end, true);
//    }
    private boolean move(Spot begin, Spot end, boolean ignoreTurn) throws Exception {
        if (!ignoreTurn && begin.getPiece().isWhite() != isWhiteTurn()) {
            throw new Exception("Wrong turn order");
        }
        if (begin.getPiece() != null && begin.getPiece().canMove(this, begin, end)) {
            if (end.getPiece() != null) {
                end.setPiece(null);
            }
            end.setPiece(begin.getPiece());
            begin.getPiece().setMoved();
            begin.setPiece(null);
            if (end.getPiece() instanceof Pawn && this.megaPawnCells.contains(new Coordinate(end.getX(), end.getY()))) {
                end.setPiece(new MegaPawn(end.getPiece().isWhite()));
                System.out.println("Pawn promoted to MegaPawn");
            }

            this.board.add(end);
            this.newBoard.put(new Coordinate(end.getX(), end.getY()), end);
            this.board.remove(begin);
            this.newBoard.remove(new Coordinate(begin.getX(), begin.getY()));
            boolean isInCheck = getKing(!isWhiteTurn()) != null ? getKing(!isWhiteTurn()).inCheck(this) : false;
            if (isInCheck) {
                notifyCheckListeners(!isWhiteTurn());
                boolean isCheckmate = kingInCheckmate();
                if (isCheckmate) {
                    notifyCheckmateListeners(!isWhiteTurn());
                }
            }
            if (Objects.equals(end.getPiece().getChessType(), "King")) {
                if (end.getPiece().isWhite())
                    kingW = (King) end.getPiece();
                else kingB = (King) end.getPiece();
            }
            System.out.printf("Move %s from [%d,%d] to [%d,%d]\n", end.getPiece(), begin.getX(), begin.getY(), end.getX(), end.getY());
            return true;
        }
        return false;
    }

    public boolean kingInCheckmate() throws Exception {
        boolean whiteInCheckmate = getKing(true) != null && getKing(true).inCheckmate(this);
        boolean blackInCheckmate = getKing(false) != null && getKing(false).inCheckmate(this);
        return whiteInCheckmate ^ blackInCheckmate;
    }

    public HashSet<Spot> getBoard() {
        return board;
    }

    public HashMap<Coordinate,Spot> getNewBoard() {
        return newBoard;
    }
    public boolean isWhiteTurn() {
        return whiteToMove;
    }
    public void setWhiteToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
    }
    public void addMove(Spot end) {
        addElement(getHashColumn(), end.getY(), end.getX());
        addElement(getHashRow(), end.getX(), end.getY());
        addElement(getHashDiagonalPositive(), end.getX(), end.getX() - end.getY());
        addElement(getHashDiagonalNegative(), end.getX(), end.getX() + end.getY());
    }

    public void deleteMove(Spot start) {
        deleteElement(getHashColumn(), start.getY(), start.getX());
        deleteElement(getHashRow(), start.getX(), start.getY());
        deleteElement(getHashDiagonalPositive(), start.getX(), start.getX() - start.getY());
        deleteElement(getHashDiagonalNegative(), start.getX(), start.getX() + start.getY());
    }

}
