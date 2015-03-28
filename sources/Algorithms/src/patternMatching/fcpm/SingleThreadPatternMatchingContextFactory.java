package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.array.ArrayBasedTable;
import patternMatching.fcpm.table.builder.IPatternMatchingConfig;
import patternMatching.fcpm.table.hash.BuildInHashTableBasedTable;
import patternMatching.fcpm.table.hash.OwnHashTableBasedTable;

public class SingleThreadPatternMatchingContextFactory implements IPatternMatchingContextFactory
{
    private IPatternMatchingConfig config;

    public SingleThreadPatternMatchingContextFactory(IPatternMatchingConfig config)
    {
        this.config = config;
    }

    @Override
    public IPatternMatchingContext create(Product[] patternSlp, Product[] textSlp)
    {
        switch (config.getAPTableType())
        {
            case Array:
                return new PatternMatchingContext(new ArrayBasedTable(patternSlp.length, textSlp.length), patternSlp, textSlp);
            case OwnHashTable:
                return new PatternMatchingContext(new OwnHashTableBasedTable(patternSlp.length, textSlp.length), patternSlp, textSlp);
            case BuildInHashTable:
                return new PatternMatchingContext(new BuildInHashTableBasedTable(patternSlp.length, textSlp.length), patternSlp, textSlp);
        }
        throw new IllegalArgumentException();
    }
}
