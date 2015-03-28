package compressingCore.dataFiltering.automatas;

public class WordRecognizer implements IAutomata
{
    private String word;
    private int[] spArray;
    private int state;

    public WordRecognizer(String word)
    {
        this.word = word.toLowerCase();
        spArray = StringHelpers.calculateSPArray(this.word);
        moveToStart();
    }

    @Override
    public boolean isAcceptState()
    {
        return word.length() == state;
    }

    @Override
    public void move(char symbol)
    {
        char lowerCaseSymbol = Character.toLowerCase(symbol);
        if (isAcceptState())
            state = spArray[state - 1];
        if (word.charAt(state) == lowerCaseSymbol)
            state++;
        else
        {
            while (word.charAt(state) != lowerCaseSymbol && state > 0)
                state = spArray[state - 1];

            if (word.charAt(state) == lowerCaseSymbol)
                state++;
            else
                moveToStart();
        }
    }

    @Override
    public void moveToStart()
    {
        state = 0;
    }
}
