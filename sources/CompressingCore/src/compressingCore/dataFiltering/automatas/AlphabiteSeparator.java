package compressingCore.dataFiltering.automatas;

public class AlphabiteSeparator implements IAutomata
{
    private IAlphabite escapedAplhabite;
    private int state;

    public AlphabiteSeparator(IAlphabite alphabite)
    {
        escapedAplhabite = alphabite;
        moveToStart();
    }

    @Override
    public boolean isAcceptState()
    {
        return state == 0;
    }

    @Override
    public void move(char symbol)
    {
        state = escapedAplhabite.contains(symbol) ? 1 : 0;
    }

    @Override
    public void moveToStart()
    {
        state = 0;
    }
}