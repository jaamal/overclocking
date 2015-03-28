package compressingCore.dataFiltering.automatas;

public interface IAutomata
{
    void move(char symbol);

    boolean isAcceptState();

    void moveToStart();
}
