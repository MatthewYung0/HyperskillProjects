package tictactoe;

import tictactoe.enums.STATUS;
import tictactoe.enums.SYMBOL;

import javax.swing.*;

public class GameManager {
    private final Board board;
    private final JLabel status;
    private boolean isReset = true;
    private boolean isXTurn = true;

    GameManager(Board board, JLabel status) {
        this.board = board;
        this.status = status;
    }

    public void setButtonSymbol(Button button) {
        if (button.getSymbol() == null) {
            if (isXTurn) {
                button.setSymbol(SYMBOL.X);
                isXTurn = false;
            } else {
                button.setSymbol(SYMBOL.O);
                isXTurn = true;
            }
            isReset = false;
            checkGameStatus();
        }
    }

    private void checkGameStatus() {
        if (!isReset) { setStatus(STATUS.PROGRESS); }
        if (isWin(SYMBOL.X)) {
            setStatus(STATUS.XWIN);
        } else if (isWin(SYMBOL.O)) {
            setStatus(STATUS.OWIN);
        } else if (isDraw()) {
            setStatus(STATUS.DRAW);
        }
        if (getStatus() != STATUS.WAITING && getStatus() != STATUS.PROGRESS) {
            isXTurn = true;
        }
    }

    private boolean isDraw() {
        Button[][] buttons = board.getButtons();
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getSymbol() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isWin(SYMBOL symbol) {
        Button[][] buttons = board.getButtons();
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getSymbol() == symbol && buttons[i][1].getSymbol() == symbol && buttons[i][2].getSymbol() == symbol) {
                return true;
            }
            if (buttons[0][i].getSymbol() == symbol && buttons[1][i].getSymbol() == symbol && buttons[2][i].getSymbol() == symbol) {
                return true;
            }
        }
        if (buttons[0][0].getSymbol() == symbol && buttons[1][1].getSymbol() == symbol && buttons[2][2].getSymbol() == symbol) {
            return true;
        }
        return buttons[0][2].getSymbol() == symbol && buttons[1][1].getSymbol() == symbol && buttons[2][0].getSymbol() == symbol;
    }

    private void setStatus(STATUS status) {
        switch (status) {
            case WAITING -> this.status.setText(String.valueOf(STATUS.WAITING));
            case PROGRESS -> this.status.setText(String.valueOf(STATUS.PROGRESS));
            case XWIN -> this.status.setText(String.valueOf(STATUS.XWIN));
            case OWIN -> this.status.setText(String.valueOf(STATUS.OWIN));
            case DRAW -> this.status.setText(String.valueOf(STATUS.DRAW));
        }
        this.status.repaint();
    }

    public STATUS getStatus() { return STATUS.fromString(this.status.getText()); }

    public void resetButtons() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board.getButtons()[row][col].setSymbol(null);
            }
        }
        setStatus(STATUS.WAITING);
        this.isReset = true;
    }

}
