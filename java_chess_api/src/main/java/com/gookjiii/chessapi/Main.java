package com.gookjiii.chessapi;

import com.gookjiii.chessapi.board_manager.Board;
import com.gookjiii.chessapi.board_manager.Coordinate;
import com.gookjiii.chessapi.board_manager.Spot;
import com.gookjiii.chessapi.entities.*;
import com.gookjiii.chessapi.listener.CheckListener;
import com.gookjiii.chessapi.listener.CheckListenerImpl;
import com.gookjiii.chessapi.listener.CheckmateListener;
import com.gookjiii.chessapi.listener.CheckmateListenerImpl;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) throws Exception {
    List<Coordinate> megaPawnList = new ArrayList<>();
    megaPawnList.add(new Coordinate(3, 3));
    Board board = new Board(megaPawnList);
    CheckListener checkListener = new CheckListenerImpl();
    CheckmateListener checkmateListener = new CheckmateListenerImpl();
    board.addCheckListener(checkListener);
    board.addCheckmateListener(checkmateListener);
    board.putPiece(new King(true), 1, 1, true);
    board.putPiece(new King(false), 5, 6, false);
    board.putPiece(new Rook(true), 3, 1, true);
    board.putPiece(new Queen(false), 0, 0, false);
    board.move(board.getSpot(1, 1), board.getSpot(0, 0));
    board.putPiece(new Queen(true), 3, 0, true);
    board.move(board.getSpot(0, 0), board.getSpot(1, 0));
    board.move(board.getSpot(3, 0), board.getSpot(2, 0));
    board.getFigure(2, 0);
    for (Spot spot : board.getBoard()) {
      System.out.printf("[%d,%d] %s\n", spot.getX(), spot.getY(), spot.getPiece());
    }
  }
}
