package patternMatching.fcpm.table.builder;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.table.IAPTable;

public interface IAPTableBuilder
{
    IAPTable build(Product[] patternSlp, Product[] textSlp);
}
