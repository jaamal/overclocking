package patternMatching.fcpm.table.concurrent;

import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.IAPTableFactory;

public class SynchronizedSetTableWrapperFactory implements IAPTableFactory
{
    private final IAPTableFactory tableFactory;

    public SynchronizedSetTableWrapperFactory(IAPTableFactory tableFactory)
    {
        this.tableFactory = tableFactory;
    }

    @Override
    public IAPTable create(int patternSize, int textSize)
    {
        return new SynchronizedTableWrapper(tableFactory.create(patternSize, textSize));
    }
}
