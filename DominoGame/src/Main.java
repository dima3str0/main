import javax.swing.*;

public class Main { // Объявление класса Main
    private static final JFrame frame = new JFrame("Domino");
    private static final GameTable table = new GameTable();

    public static void main(String[] args) {
        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        startGame(); // Вызов метода для начала игры
    }

    private static void startGame() { // Метод начала игры
        table.setLayout(null);
        frame.add(table);
        table.startGame();
        table.setSize(1200, 800);
        frame.setVisible(true);
    }
}