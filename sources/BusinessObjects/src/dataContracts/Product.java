package dataContracts;

public class Product
{
    public final boolean isTerminal;
    public final char symbol;
    public final long first, second;

    public Product(char symbol)
    {
        this.isTerminal = true;
        this.symbol = symbol;
        this.first = this.second = -1;
    }

    public Product(long first, long second)
    {
        this.isTerminal = false;
        this.symbol = 0;
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product))
            return false;
        Product other = (Product) object;
        if (isTerminal != other.isTerminal)
            return false;
        if (isTerminal)
            return symbol == other.symbol;

        return first == other.first && second == other.second;
    }

    @Override
    public int hashCode() {
        return isTerminal ? symbol : (int) (first * 997 + second * 1000003);
    }
}
