package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.arithmeticProgression.ArithmeticProgression;

public final class LocalSearchResult implements IEditableLocalSearchResult
{
    private ArithmeticProgression first;
    private ArithmeticProgression second;

    public void add(ArithmeticProgression ap) throws IllegalStateException
    {
        if (ap.isEmpty())
            return;
        if (first == null)
        {
            first = ap;
            return;
        }
        if (second == null)
        {
            ArithmeticProgression merge = first.safeMerge(ap);
            if (merge != null)
            {
                first = merge;
            } else
            {
                second = ap;
            }
        } else
        {
            ArithmeticProgression merge = second.safeMerge(ap);
            if (merge == null)
                throw new IllegalStateException();
            second = merge;
        }
    }

    public boolean isEmpty()
    {
        return first == null;
    }

    public int size()
    {
        if (first == null)
            return 0;
        if (second == null)
            return 1;
        return 2;
    }

    public ArithmeticProgression get(int index)
    {
        if (index == 0)
            return first;
        if (index == 1)
            return second;
        throw new IllegalStateException();
    }

    @Override
    public String toString()
    {
        if (first == null)
            return "empty";
        if (second == null)
            return first.toString();
        return String.format("%s, %s", first, second);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof LocalSearchResult)) return false;

        LocalSearchResult that = (LocalSearchResult) o;

        if (first != null ? !first.equals(that.first) : that.first != null) return false;
        if (second != null ? !second.equals(that.second) : that.second != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}
