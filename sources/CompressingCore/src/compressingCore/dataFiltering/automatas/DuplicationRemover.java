package compressingCore.dataFiltering.automatas;

public class DuplicationRemover implements IAutomata
{
    private IAlphabite duplicationAlphabite;
    private int state;
    private char lastIgnoringLetter;

    public DuplicationRemover(IAlphabite alphabite)
    {
        duplicationAlphabite = alphabite;
        moveToStart();
    }

    @Override
    public boolean isAcceptState()
    {
        return state != 2;
    }

    @Override
    public void move(char symbol)
    {
        if (duplicationAlphabite.contains(symbol))
        {
            if (state == 0 || (state == 2 && symbol != lastIgnoringLetter))
                state = 1;
            else if (state == 1)
            {
                lastIgnoringLetter = symbol;
                state = 2;
            }
        } else
            moveToStart();
    }

    @Override
    public void moveToStart()
    {
        state = 0;
        lastIgnoringLetter = 0;
    }
}