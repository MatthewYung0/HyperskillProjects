package tictactoe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Board extends JPanel {
    private GameManager gameManager;
    private Button[][] buttons = new Button[3][3];

    public Board() {
        setName("Board");
        setLayout(new GridLayout(3, 3, 10, 10));
        setBackground(Color.LIGHT_GRAY);
        setSize(500, 500);
        setLocation(0,0);
        setBorder(new EmptyBorder(10,10,10,10));
    }

    public Button[][] getButtons() {
        return this.buttons;
    }

    public void setButtons(Button[][] buttons) {
        this.buttons = buttons;
    }
}
