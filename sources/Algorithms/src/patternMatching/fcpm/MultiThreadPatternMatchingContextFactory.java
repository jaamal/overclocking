package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.APTableStatisticsFactory;
import patternMatching.fcpm.table.DebuggingAPTable;
import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.array.ArrayBasedTable;
import patternMatching.fcpm.table.builder.IPatternMatchingConfig;
import patternMatching.fcpm.table.hash.BuildInHashTableBasedTable;
import patternMatching.fcpm.table.hash.OwnHashTableBasedTable;

public final class MultiThreadPatternMatchingContextFactory implements IPatternMatchingContextFactory
{
    private final IPatternMatchingConfig config;

    public MultiThreadPatternMatchingContextFactory(IPatternMatchingConfig config)
    {
        this.config = config;
    }

    @Override
    public IPatternMatchingContext create(Product[] patternSlp, Product[] textSlp)
    {
        switch (config.getAPTableType())
        {
            case Array: {
                IAPTable table = new ArrayBasedTable(patternSlp.length, textSlp.length);
                if(config.withStatistics())
                    table = new DebuggingAPTable(table, new APTableStatisticsFactory());
                return new PatternMatchingContext(table, patternSlp, textSlp);
            }
            case OwnHashTable: {
                IAPTable table = new OwnHashTableBasedTable(patternSlp.length, textSlp.length);
                if(config.withStatistics())
                    table = new DebuggingAPTable(table, new APTableStatisticsFactory());
                return new PatternMatchingContext(table, patternSlp, textSlp);
            }
            case  BuildInHashTable:
                IAPTable table = new BuildInHashTableBasedTable(patternSlp.length, textSlp.length);
                if(config.withStatistics())
                    table = new DebuggingAPTable(table, new APTableStatisticsFactory());
                return new PatternMatchingContext(table, patternSlp, textSlp);
        }
        throw new IllegalArgumentException();
    }
}
