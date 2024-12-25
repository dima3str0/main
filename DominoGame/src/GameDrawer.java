import java.awt.*; // Импорт классов AWT

public class GameDrawer { // Объявление класса GameDrawer
    private final Color backColor = Color.gray;
    private final Color textColor = Color.black;
    private final int startX = 400; // Начальная X координата для отрисовки руки
    private final int startY = 680; // Начальная Y координата для отрисовки руки
    private final int dominoWidth = 40;
    private final int dominoHeight = 80;
    private Graphics2D grap;

    public GameDrawer(Graphics2D grap) { // Конструктор класса GameDrawer
        this.grap = grap;
    }

    // Метод для установки Graphics2D
    public void setGraphics(Graphics2D grap){
        this.grap = grap;
    }

    // Метод для отрисовки объекта, реализующего интерфейс Drawable
    public void draw(Drawable drawable) {
        if (grap == null) return;
        drawable.draw(grap);
    }


    // Метод для отрисовки руки
    public void drawHand(AbstractHand hand) {
        if (grap == null) return;
        for (int i = 0; i < hand.size(); i++) {
            grap.setColor(backColor);
            grap.fillRect(startX + (i * 60), startY, dominoWidth, dominoHeight);
            grap.setColor(textColor); // Установка цвета текста
            grap.drawString("" + hand.getDomino(i).getFirstSite(), startX + 15 + (i * 60), startY + 25);
            grap.drawString("" + hand.getDomino(i).getSecondSite(), startX + 15 + (i * 60), startY + 55);
        }
    }

    // Метод для отрисовки колоды
    public void drawDeck(int deckSize){
        if (grap == null) return;
        grap.setColor(backColor);
        grap.fillRect(20,380,80,40);
        grap.setColor(textColor);
        grap.drawString("Deck: " + deckSize, 40, 400);
    }

    // Метод для вывода счёта
    public void drawScore(int playerScore, int botScore){
        if (grap == null) return;
        grap.drawString("Player score: " + playerScore, 600, 400);
        grap.drawString("Bot score: " + botScore, 600, 420);
    }
}