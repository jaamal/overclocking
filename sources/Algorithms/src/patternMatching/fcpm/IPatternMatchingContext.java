package patternMatching.fcpm;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;

public interface IPatternMatchingContext
{
    Product getPattern(int index);

    Product getText(int index);

    IAPTable getAPTable();
}
