package data.filters.automatas;


import java.util.HashSet;

public class Alphabite implements IAlphabite
{
    private HashSet<Character> charsSet;

    public Alphabite(char[] symbols)
    {
        charsSet = new HashSet<Character>();
        for (char ch : symbols)
            charsSet.add(ch);
    }

    @Override
    public boolean contains(char ch)
    {
        return charsSet.contains(ch);
    }
}
