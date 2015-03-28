package tests.unit.Algorithms.patternMatching.arithmeticProgression.table;

import patternMatching.fcpm.preprocessing.Product;

public class SLPHelper {
    public static String getText(Product[] slp, int index) {
        Product product = slp[index];
        if (product.IsTerminal)
            return product.FirstLetter + "";
        return getText(slp, product.FirstProduct) + getText(slp, product.SecondProduct);
    }

}
