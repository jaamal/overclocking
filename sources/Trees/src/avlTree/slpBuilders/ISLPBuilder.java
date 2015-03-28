package avlTree.slpBuilders;

import dataContracts.Product;
import dataContracts.SLPStatistics;

public interface ISLPBuilder {
    long addRule(Product product);
    SLPStatistics getStatistics();
    Product[] toNormalForm();
    String getProductString();
}
