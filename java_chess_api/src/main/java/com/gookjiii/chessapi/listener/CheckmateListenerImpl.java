package com.gookjiii.chessapi.listener;

public class CheckmateListenerImpl implements CheckmateListener {
    @Override
    public void onCheckmate(boolean isWhite) {
        System.out.println("Checkmate for " + (isWhite ? "White" : "Black") + "!");
    }
}
