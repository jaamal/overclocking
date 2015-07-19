package data.filters.automatas;

public interface IAutomata
{
    void move(char symbol);

    boolean isAcceptState();

    void moveToStart();
}
