import javax.swing.*;

public class DominoButton extends JButton {
    private final int position; // Поле position также делаем final
    private Domino domino; // Ссылка на домино, которое представляет кнопка
    public DominoButton(int position, Domino domino){
        this.position = position;
        this.domino = domino;
    }
    public int getPosition(){
        return position;
    }

    public Domino getDomino(){
        return domino;
    }

    public void setRemove(){
        setVisible(false);
    }
}