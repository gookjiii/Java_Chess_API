package com.gookjiii.chessapi.listener;

public class CheckListenerImpl implements CheckListener {
    @Override
    public void onCheck(boolean isWhite) {
        System.out.println("Check for " + (isWhite ? "White" : "Black") + "!");
    }
}
