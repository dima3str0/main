import java.util.LinkedList; // Импорт класса LinkedList
import java.util.List; // Импорт класса List

public abstract class AbstractHand { // Объявление абстрактного класса AbstractHand
    protected List<Domino> hand = new LinkedList<>(); // Список домино в руке

    public void addDomino(Domino domino) { // Метод добавления домино в руку
        hand.add(domino);
    }

    public void removeDomino(Domino domino) { // Метод удаления домино из руки
        hand.remove(domino);
    }

    public Domino getDomino(int index) { // Метод получения домино по индексу
        return hand.get(index);
    }

    public int size() { // Метод получения размера руки
        return hand.size();
    }

    public List<Domino> getHand() { // Метод получения списка домино в руке
        return hand;
    }

}