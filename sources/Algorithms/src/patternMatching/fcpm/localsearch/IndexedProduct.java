package patternMatching.fcpm.localsearch;

import patternMatching.fcpm.preprocessing.Product;

public final class IndexedProduct extends Product {
    public final int Index;

    public IndexedProduct(int index, Product product) {
        super(product);
        Index = index;
    }
}
