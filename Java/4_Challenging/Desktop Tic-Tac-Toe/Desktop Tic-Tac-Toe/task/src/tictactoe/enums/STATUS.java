package tictactoe.enums;

public enum STATUS {
    WAITING("Game is not started"),
    PROGRESS("Game in progress"),
    XWIN("X wins"),
    OWIN("O wins"),
    DRAW("Draw");

    private final String text;

    STATUS(String text) {
        this.text = text;
    }

    public static STATUS fromString(String text) {
        for (STATUS status : STATUS.values()) {
            if (status.text.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString() {
        return text;
    }
}
