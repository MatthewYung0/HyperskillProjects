package tictactoe;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Button[][] buttons = new Button[3][3];

    public Board() {
        setName("Board");
        setLayout(new GridLayout(3, 3, 5 , 5));
        setBackground(Color.WHITE);
        initButtons();
    }

    private void initButtons() {
        char[] letters = {'A', 'B', 'C'};
        for (int row = 3; row >= 1; row--) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button(letters[col] + String.valueOf(row));
                buttons[3 - row][col] = button;
                add(button);
            }
        }
    }
}
