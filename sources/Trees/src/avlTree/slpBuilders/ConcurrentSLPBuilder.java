package avlTree.slpBuilders;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import dataContracts.Product;
import dataContracts.SLPModel;

public class ConcurrentSLPBuilder implements ISLPBuilder {
    private AtomicLong count = new AtomicLong(0);
    private ConcurrentHashMap<Long, Product> rulesIndex;
    private ConcurrentHashMap<Product, Long> productsIndex;

    public ConcurrentSLPBuilder() {
        productsIndex = new ConcurrentHashMap<>();
        rulesIndex = new ConcurrentHashMap<>();
    }

    public long append(Product product) {
        long fromNumber;
        Long mapValue = productsIndex.get(product);
        if (mapValue != null) {
            if (mapValue == -1) {
                mapValue = waitFillingAndGet(product);
            }
            fromNumber = mapValue;
        } else {
            if (productsIndex.putIfAbsent(product, (long) -1) != null) {
                fromNumber = waitFillingAndGet(product);
            } else {
                fromNumber = count.getAndIncrement();
                if (!product.isTerminal && (product.first >= fromNumber || product.second >= fromNumber))
                    throw new InvalidProductionRuleException(fromNumber, product);

                rulesIndex.put(fromNumber, product);
                productsIndex.put(product, fromNumber);
            }
        }
        return fromNumber;
    }
    
    @Override
    public SLPModel toSLPModel() {
        SLPModel result = new SLPModel(rulesIndex);
        rulesIndex = null;
        productsIndex = null;
        count = null;
        return result;
    }

    private Long waitFillingAndGet(Product product) {
        while (true) {
            Long mapValue = productsIndex.get(product);
            if (mapValue != -1) {
                return mapValue;
            }

            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
