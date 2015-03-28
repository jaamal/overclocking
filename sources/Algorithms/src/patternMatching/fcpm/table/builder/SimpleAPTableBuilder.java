package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.IPatternMatchingContext;
import patternMatching.fcpm.IPatternMatchingContextFactory;
import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.IAPTableCellCalculator;


public final class SimpleAPTableBuilder implements IAPTableBuilder
{
    private IPatternMatchingContextFactory contextFactory;
    private IAPTableCellCalculator cellCalculator;

    public SimpleAPTableBuilder(IPatternMatchingContextFactory contextFactory, IAPTableCellCalculator cellCalculator)
    {
        this.contextFactory = contextFactory;
        this.cellCalculator = cellCalculator;
    }

    @Override
    public IAPTable build(Product[] patternSlp, Product[] textSlp)
    {
        IPatternMatchingContext context = contextFactory.create(patternSlp, textSlp);

        for (int patternIndex = 0; patternIndex < patternSlp.length; ++patternIndex)
        {
            for (int textIndex = 0; textIndex < textSlp.length; ++textIndex)
            {
                context.getAPTable().set(patternIndex, textIndex, cellCalculator.calculate(context, patternIndex, textIndex));
            }
        }
        return context.getAPTable();
    }
}



