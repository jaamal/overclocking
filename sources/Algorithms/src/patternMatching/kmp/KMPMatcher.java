package patternMatching.kmp;

import patternMatching.IPatternMatcher;

public class KMPMatcher implements IPatternMatcher
{

    private String text;
    private String pattern;
    private int prefixFunction[];

    //TODO: this class contains two responsibilities: find prefix function (better to implement in a factory) and KMP algorithm;
    public KMPMatcher(String text, String pattern)
    {
        this.text = text;
        this.pattern = pattern;
        this.prefixFunction = createTable();
    }

    public static KMPMatcher newMatcher(String text, String pattern)
    {
        if (text.isEmpty())
            throw new RuntimeException("Text is empty");
        if (pattern.isEmpty())
            throw new RuntimeException("Pattern is empty");
        return new KMPMatcher(text, pattern);
    }

    public boolean contains()
    {
        int i = 0, j = 0;
        int n = text.length();
        int m = pattern.length();
        while (i < n)
        {
            while (j >= 0 && text.charAt(i) != pattern.charAt(j)) j = prefixFunction[j];
            i++;
            j++;
            if (j == m)
            {
                return true;
            }
        }
        return false;
    }

    public int count()
    {
        throw new RuntimeException("Method not impplemented.");
    }

    @Override
    public boolean contains(int position)
    {
        throw new RuntimeException("Not implemented");
    }

    private int[] createTable()
    {
        char[] w = pattern.toCharArray();
        int[] t = new int[w.length + 2];
        int i = 2;
        int j = 0;
        t[0] = -1;
        t[1] = 0;
        while (i < w.length)
        {
            if (w[i - 1] == w[j])
            {
                t[i] = j + 1;
                j++;
                i++;
            } else if (j > 0)
            {
                j = t[j];
            } else
            {
                t[i] = 0;
                i++;
                j = 0;
            }
        }
        return t;
    }
}
