package data.filters.automatas;


public class AutomataFactory implements IAutomataFactory
{
    private IAlphabetFactory alphabetFactory;

    public AutomataFactory(IAlphabetFactory alphabetFactory)
    {
        this.alphabetFactory = alphabetFactory;
    }

    @Override
    public IAutomata createAutomata(AutomataType automataType)
    {
        switch (automataType)
        {
            case DNA:
                IAutomata startAutomata = new WordRecognizer("origin");
                IAutomata importAutomata = new AlphabiteRecognizer(alphabetFactory.create(AlphabiteType.DNA));
                IAutomata endAutomata = new WordRecognizer("//");
                return new WordsBoundedAutomata(startAutomata, importAutomata, endAutomata);
            default:
                throw new UnknownAutomataTypeException(automataType);
        }
    }
}
