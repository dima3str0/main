public class Domino {
    private final int firstSite; // поля лучше сделать final, если их нельзя изменить
    private final int secondSite;

    public Domino(int firstSite, int secondSite){
        this.firstSite = firstSite;
        this.secondSite = secondSite;
    }

    public int getFirstSite() {
        return firstSite;
    }

    public int getSecondSite() {
        return secondSite;
    }

    @Override
    public String toString() { // Добавим toString для удобства отладки
        return "[" + firstSite + "|" + secondSite + "]";
    }

    //Метод для создания перевернутого домино
    public Domino flip(){
        return new Domino(secondSite,firstSite);
    }
}