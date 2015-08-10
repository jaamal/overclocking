package productEnumerator;

import java.util.ArrayList;
import java.util.HashMap;
import dataContracts.Product;
import dataContracts.SLPModel;

public class ProductEnumerator implements IProductEnumerator
{
    private ArrayList<Product> rules;
    private HashMap<Product, Long> productsIndex;

    public ProductEnumerator()
    {
        productsIndex = new HashMap<>();
        rules = new ArrayList<>();
    }

    public long append(Product product)
    {
        long fromNumber;
        Long mapValue = productsIndex.get(product);
        if (mapValue != null) {
            fromNumber = mapValue;
        } else {
            fromNumber = rules.size();
            if (!product.isTerminal && (product.first >= fromNumber || product.second >= fromNumber))
                throw new RuntimeException(String.format("Product with number %s links on products with greater numbers: %s, %s", 
                                           fromNumber, product.first, product.second));

            productsIndex.put(product, fromNumber);
            rules.add(product);
        }
        return fromNumber;
    }
    
    @Override
    public SLPModel toSLPModel() {
        HashMap<Long, Product> rulesIndex = new HashMap<>();
        if (!rules.isEmpty()) {
            int rulesSize = rules.size();
            for (int i = 0; i < rulesSize; i++) {
                rulesIndex.put((long) i, rules.get(i));
            }
        }
        SLPModel result = new SLPModel(rulesIndex);
        rules = null;
        return result;
    }
}
