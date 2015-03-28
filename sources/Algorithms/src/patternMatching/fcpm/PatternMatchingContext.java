package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;

public final class PatternMatchingContext implements IPatternMatchingContext
{
    private final IAPTable table;
    private final Product[] patternSlp;
    private final Product[] textSlp;

    public PatternMatchingContext(IAPTable table, Product[] patternSlp, Product[] textSlp)
    {
        this.table = table;
        this.patternSlp = patternSlp;
        this.textSlp = textSlp;
    }

    @Override
    public Product getPattern(int index)
    {
        return patternSlp[index];
    }

    @Override
    public Product getText(int index)
    {
        return textSlp[index];
    }

    @Override
    public IAPTable getAPTable()
    {
        return table;
    }


}
