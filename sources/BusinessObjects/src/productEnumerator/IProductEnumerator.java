package productEnumerator;

import dataContracts.Product;
import dataContracts.SLPModel;

public interface IProductEnumerator {
    long append(Product product);
    SLPModel toSLPModel();
}
