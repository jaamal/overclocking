package patternMatching.fcpm.table.array;

import patternMatching.fcpm.table.IAPTable;
import patternMatching.fcpm.table.IAPTableFactory;

public final class ArrayBasedTableFactory implements IAPTableFactory
{
    @Override
    public IAPTable create(int patternSize, int textSize)
    {
        return new ArrayBasedTable(patternSize, textSize);
    }
}
