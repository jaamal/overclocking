package data.filters.automatas;

public class AlphabiteRecognizer implements IAutomata
{
    private IAlphabite alphabite;
    private int state;

    public AlphabiteRecognizer(IAlphabite alphabite)
    {
        this.alphabite = alphabite;
        moveToStart();
    }

    @Override
    public boolean isAcceptState()
    {
        return state == 1;
    }

    @Override
    public void move(char symbol)
    {
        state = alphabite.contains(symbol) ? 1 : 0;
    }

    @Override
    public void moveToStart()
    {
        state = 0;
    }
}