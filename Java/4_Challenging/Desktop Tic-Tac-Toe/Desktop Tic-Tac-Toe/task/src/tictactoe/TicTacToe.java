package tictactoe;

import tictactoe.enums.STATUS;
import tictactoe.utils.RoundButton;

import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    private Board board;
    private JLabel status;
    private GameManager gameManager;

    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setSize(500, 540);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        /* ---Order is important!--- */
        initBoard();
        JPanel supportBar = new JPanel();
        initSupportBar(supportBar, board.getBackground());
        initStatus(supportBar);
        initGameManager();
        initButtons();
        initButtonReset(supportBar);
        /* ---Order is important!--- */

        setVisible(true);
    }

    private void initBoard() {
        board = new Board();
        add(board, BorderLayout.CENTER);
    }

    private void initButtons() {
        char[] letters = {'A', 'B', 'C'};
        Button[][] buttons = board.getButtons();
        for (int row = 3; row >= 1; row--) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(letters[col] + String.valueOf(row));
                button.setSymbol(null);
                button.addActionListener(event -> {
                    if (gameManager.getStatus() == STATUS.WAITING || gameManager.getStatus() == STATUS.PROGRESS) {
                        gameManager.setButtonSymbol(button);
                    }
                });
                buttons[3 - row][col] = button;
                board.add(button);
            }
        }
        board.setButtons(buttons);
    }

    private void initSupportBar(JPanel supportBar, Color backgroundColor) {
        supportBar.setPreferredSize(new Dimension(getWidth(), 40));
        supportBar.setLayout(null);
        supportBar.setBackground(backgroundColor);
        add(supportBar, BorderLayout.PAGE_END);
    }

    private void initStatus(JPanel supportBar) {
        status = new JLabel(String.valueOf(STATUS.WAITING));
        status.setName("LabelStatus");
        status.setForeground(Color.BLACK);
        status.setSize(200, 15);
        status.setLocation(10, 10);
        supportBar.add(status);
    }

    private void initButtonReset(JPanel supportBar) {
        JButton resetButton = new RoundButton("Reset", Color.BLACK);
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(event -> {
            gameManager.resetButtons();
        });
        resetButton.setBackground(Color.BLACK);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBounds(getWidth() - 90, 5, 80, 25);
        resetButton.setLocation(getWidth() - 90, 5);
        supportBar.add(resetButton);
    }

    private void initGameManager() {
        gameManager = new GameManager(this.board, this.status);
    }

}