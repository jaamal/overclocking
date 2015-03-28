package dataContracts;

public class LZFactorDef extends FactorDef
{
    public Long offset = null;

    public LZFactorDef(
            boolean isTerminal,
            long beginPosition,
            long length,
            char symbol)
    {
        super(isTerminal, beginPosition, length, (int) symbol);
    }
}
