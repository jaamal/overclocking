package avlTree.slpBuilders;

import dataContracts.Product;
import dataContracts.SLPModel;

public interface ISLPBuilder {
    long append(Product product);
    SLPModel toSLPModel();
}
