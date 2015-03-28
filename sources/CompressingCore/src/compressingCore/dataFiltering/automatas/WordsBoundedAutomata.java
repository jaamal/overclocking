package compressingCore.dataFiltering.automatas;

public class WordsBoundedAutomata implements IAutomata
{
    private IAutomata startAutomata;
    private IAutomata importAutomata;
    private IAutomata endAutomata;
    private int state;

    public WordsBoundedAutomata(IAutomata startAutomata, IAutomata importAutomata, IAutomata endAutomata)
    {
        this.startAutomata = startAutomata;
        this.importAutomata = importAutomata;
        this.endAutomata = endAutomata;
        moveToStart();
    }

    @Override
    public boolean isAcceptState()
    {
        return importAutomata.isAcceptState();
    }

    @Override
    public void move(char symbol)
    {
        if (state == 0)
        {
            startAutomata.move(symbol);
            if (startAutomata.isAcceptState())
            {
                startAutomata.moveToStart();
                state = 1;
            }
        } else
        {
            importAutomata.move(symbol);
            endAutomata.move(symbol);
            if (endAutomata.isAcceptState())
            {
                importAutomata.moveToStart();
                endAutomata.moveToStart();
                state = 0;
            }
        }
    }

    @Override
    public void moveToStart()
    {
        state = 0;
        startAutomata.moveToStart();
        importAutomata.moveToStart();
        endAutomata.moveToStart();
    }
}
