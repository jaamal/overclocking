package dataContracts;

public class FactorDef
{
    //NOTE: we can serialize begin + length or symbol in two integers with restrictions on positions
    //TODO: delete this const
    public final static int SIZE_IN_BYTES = 2 * 8;
    
    public final long beginPosition;
    public final long length;
    public final int symbol;
    public final boolean isTerminal;
    
    public FactorDef(long beginPosition, long length)
    {
        this.beginPosition = beginPosition;
        this.length = length;
        this.isTerminal = false;
        this.symbol = 0;
    }

    public FactorDef(char symbol)
    {
        this.isTerminal = true;
        this.symbol = symbol;
        this.beginPosition = -1;
        this.length = -1;
    }
    
    public long getEndPosition() {
        return beginPosition + length;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (beginPosition ^ (beginPosition >>> 32));
        result = prime * result + (isTerminal ? 1231 : 1237);
        result = prime * result + (int) (length ^ (length >>> 32));
        result = prime * result + symbol;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FactorDef other = (FactorDef) obj;
        if (beginPosition != other.beginPosition)
            return false;
        if (isTerminal != other.isTerminal)
            return false;
        if (length != other.length)
            return false;
        if (symbol != other.symbol)
            return false;
        return true;
    }
}
