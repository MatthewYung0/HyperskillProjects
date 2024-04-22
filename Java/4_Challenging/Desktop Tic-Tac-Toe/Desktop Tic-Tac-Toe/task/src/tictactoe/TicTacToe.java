package tictactoe;

import tictactoe.utils.RoundSquareBorder;

import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    private Board board;

    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setSize(500, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        initBoard();
        setVisible(true);
    }

    private void initBoard() {
        board = new Board();
        add(board);
    }
}