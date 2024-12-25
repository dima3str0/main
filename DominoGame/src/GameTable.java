import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.IntStream;

public class GameTable extends JPanel implements Drawable { // Класс GameTable, реализует интерфейс Drawable
    private Graphics2D grap; // Объект Graphics2D
    private final Stack<Domino> deck = new Stack<>(); // Колода
    private final PlayerHand playerHand = new PlayerHand(); // Рука игрока
    private final BotHand botHand = new BotHand(); // Рука бота
    private final LinkedList<Domino> tablePosition = new LinkedList<>(); // Позиция домино на столе
    private final List<DominoButton> playerButtons = new ArrayList<>(); // Кнопки домино игрока
    private GameDrawer drawer; // Объект GameDrawer

    public GameTable() { // Конструктор GameTable
        setLayout(null); // Установка null layout
    }

    @Override
    public void paintComponent(Graphics g) { // Метод для отрисовки компонента
        super.paintComponent(g);
        this.grap = (Graphics2D) g;
        if (drawer == null) {
            drawer = new GameDrawer(grap);
        } else {
            drawer.setGraphics(grap);
        }
        validate();

        drawer.draw(this);
        drawer.drawHand(botHand);
        drawer.drawDeck(deck.size());
        drawer.drawHand(playerHand);

        checkEndGame();
    }

    // Реализация метода draw() для интерфейса Drawable
    @Override
    public void draw(Graphics2D g) {
        drawTable(g);
    }


    // Метод отрисовки стола
    private void drawTable(Graphics2D g) {
        int[] xPosition = {560}; // Инициализация начальной координаты x
        int[] yPosition = {150}; // Инициализация начальной координаты y
        final int dominoWidth = 80; // Ширина домино
        final int dominoHeight = 40; // Высота домино
        final Color backColor = Color.gray;
        final Color textColor = Color.black;

        IntStream.range(0, tablePosition.size()).forEach(i -> { // Проход по всем домино на столе
            Domino domino = tablePosition.get(i); // Получение текущего домино
            if (i > 10) { // Определение позиции домино на столе
                xPosition[0] -= 85;
                g.setColor(backColor);
                g.fillRect(xPosition[0] + 40, yPosition[0], dominoWidth, dominoHeight);
                g.setColor(textColor);
                g.drawString("" + domino.getSecondSite(), xPosition[0] + 55, yPosition[0] + 25);
                g.drawString("" + domino.getFirstSite(), xPosition[0] + 95, yPosition[0] + 25);
            } else if (i > 5) {
                yPosition[0] += 85;
                g.setColor(backColor);
                g.fillRect(xPosition[0] + 40, yPosition[0] - dominoHeight, dominoHeight, dominoWidth);
                g.setColor(textColor);
                g.drawString("" + domino.getFirstSite(), xPosition[0] + 55, yPosition[0] - 20);
                g.drawString("" + domino.getSecondSite(), xPosition[0] + 55, yPosition[0] + 15);
            } else {
                xPosition[0] += 85;
                g.setColor(backColor);
                g.fillRect(xPosition[0], yPosition[0], dominoWidth, dominoHeight);
                g.setColor(textColor); // Установка цвета текста
                g.drawString("" + domino.getFirstSite(), xPosition[0] + 15, yPosition[0] + 25);
                g.drawString("" + domino.getSecondSite(), xPosition[0] + 55, yPosition[0] + 25);
            }
        });
    }


    private void checkEndGame() {
        if (deck.isEmpty() || playerHand.size() == 0) {
            if (canPlayerMove()) return;
            endGame();
        }
    }


    private boolean canPlayerMove() { // Метод проверки возможности хода игрока
        if (tablePosition.isEmpty()) return true;

        Domino table = tablePosition.getLast();
        for (Domino domino : playerHand.getHand()) {
            if (domino.getFirstSite() == table.getSecondSite() || domino.getSecondSite() == table.getSecondSite()) { // Проверка возможности хода
                return true;
            }
        }
        return false;
    }

    public void startGame() {
        generateDeck();
        dealHands(); //
        tablePosition.add(deck.pop());
        createDeckButton();
        setPlayerButtons();
    }


    private void generateDeck() { // Метод генерации колоды
        List<Domino> allDominos = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                allDominos.add(new Domino(i, j));
            }
        }
        Random random = new Random();
        for (int i = 0; i < 14; i++) {
            int randomPos = random.nextInt(allDominos.size());
            deck.add(allDominos.get(randomPos));
            allDominos.remove(randomPos);
        }
    }


    private void dealHands() { // Метод раздачи рук
        Random random = new Random();
        List<Domino> allDominos = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                allDominos.add(new Domino(i, j));
            }
        }
        for (int i = 0; i < 7; i++) {
            int randomPos = random.nextInt(allDominos.size());
            playerHand.addDomino(allDominos.get(randomPos));
            allDominos.remove(randomPos);
        }

        for (int i = 0; i < 7; i++) {
            int randomPos = random.nextInt(allDominos.size());
            botHand.addDomino(allDominos.get(randomPos));
            allDominos.remove(randomPos);
        }
    }

    private void createDeckButton(){ // Метод создания кнопки колоды
        JButton buttonDeck = new JButton();
        buttonDeck.setBounds(20,380,80,40);
        buttonDeck.setContentAreaFilled(false);
        buttonDeck.addActionListener(e -> {
            if (!deck.isEmpty()){
                playerHand.addDomino(deck.pop());
                setPlayerButtons();
                repaint();
            }
        });
        add(buttonDeck);
    }

    private void setPlayerButtons(){ // Метод установки кнопок домино игрока
        for(DominoButton button : playerButtons) {
            remove(button);
        }
        playerButtons.clear();
        for(int i = 0; i < playerHand.size(); i++) {
            Domino domino = playerHand.getDomino(i);
            DominoButton dominoButton = new DominoButton(i, domino);
            playerButtons.add(dominoButton);

            dominoButton.setContentAreaFilled(false);
            dominoButton.setBounds(400 + (i * 60), 680, 40, 80);

            add(dominoButton);
            dominoButton.addActionListener(e -> playerMove(dominoButton));
        }
        repaint();
    }

    private void playerMove(DominoButton dominoButton) {
        Domino selectedDomino = dominoButton.getDomino();
        Domino lastTableDomino = tablePosition.getLast();
        if (selectedDomino.getFirstSite() == lastTableDomino.getSecondSite()) { //
            tablePosition.add(selectedDomino);
            playerHand.removeDomino(selectedDomino);
            botMotion();
        }
        else if (selectedDomino.getSecondSite() == lastTableDomino.getSecondSite()) {
            tablePosition.add(selectedDomino.flip());
            playerHand.removeDomino(selectedDomino);
            botMotion();
        }
        setPlayerButtons();
    }


    private void botMotion() { // Метод хода бота
        Domino tableDomino = tablePosition.get(tablePosition.size() - 1);
        for (int i = 0; i < botHand.size(); i++) {
            Domino botDomino = botHand.getDomino(i);
            if (botDomino.getFirstSite() == tableDomino.getSecondSite()) {
                tablePosition.add(botDomino);
                botHand.removeDomino(botDomino);
                if (botHand.size() == 0) {
                    endGame();
                }
                return;
            } else if (botDomino.getSecondSite() == tableDomino.getSecondSite()) {
                tablePosition.add(botDomino.flip());
                botHand.removeDomino(botDomino);
                if (botHand.size() == 0) {
                    endGame();
                }
                return;
            }
        }
        if (deck.isEmpty()) {
            endGame();
        } else {
            botHand.addDomino(deck.pop());
            repaint();
            botMotion();
        }
    }
    private void endGame() { // Метод завершения игры
        int playerScore = 0;
        int botScore = 0;

        for (Domino domino : playerHand.getHand()) {
            playerScore += domino.getFirstSite() + domino.getSecondSite();
        }

        for (Domino domino : botHand.getHand()) {
            botScore += domino.getFirstSite() + domino.getSecondSite();
        }
        drawer.drawScore(playerScore,botScore);
        removeAll();
    }
}