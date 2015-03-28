package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.APTableStatisticsFactory;
import patternMatching.fcpm.table.DebuggingAPTable;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.array.ArrayBasedTable;
import patternMatching.fcpm.table.builder.IPatternMatchingConfig;
import patternMatching.fcpm.table.concurrent.SynchronizedTableWrapper;
import patternMatching.fcpm.table.concurrent.hash.ConcurrentBuildInHashTableBasedTable;

public final class MultiThreadWithSharedTablePatternMatchingContextFactory implements IPatternMatchingContextFactory
{
    private final IPatternMatchingConfig config;

    public MultiThreadWithSharedTablePatternMatchingContextFactory(IPatternMatchingConfig config)
    {
        this.config = config;
    }

    @Override
    public IPatternMatchingContext create(Product[] patternSlp, Product[] textSlp)
    {
        switch (config.getAPTableType())
        {
            case Array: {
                IAPTable table = new SynchronizedTableWrapper(new ArrayBasedTable(patternSlp.length, textSlp.length));
                if(config.withStatistics())
                    table = new DebuggingAPTable(table, new APTableStatisticsFactory());
                return new PatternMatchingContext(table, patternSlp, textSlp);
            }
            case OwnHashTable:
                throw new RuntimeException();
            case BuildInHashTable: {
                IAPTable table = new ConcurrentBuildInHashTableBasedTable(patternSlp.length, textSlp.length);
                if(config.withStatistics())
                    throw new RuntimeException();
                return new PatternMatchingContext(table, patternSlp, textSlp);
            }
        }
        throw new IllegalArgumentException();
    }
}
