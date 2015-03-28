package avlTree.slpBuilders;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import dataContracts.Product;
import dataContracts.SLPStatistics;

public class ConcurrentSLPBuilder implements ISLPBuilder {
    public final AtomicLong count = new AtomicLong(0);
    public final ConcurrentHashMap<Long, Product> rules;
    public final ConcurrentHashMap<Long, SLPStatistics> statistics;
    public final ConcurrentHashMap<Product, Long> hashMap;

    public ConcurrentSLPBuilder() {
        hashMap = new ConcurrentHashMap<>();
        rules = new ConcurrentHashMap<>();
        statistics = new ConcurrentHashMap<>();
    }

    public long addRule(Product product) {
        long fromNumber;
        Long mapValue = hashMap.get(product);
        if (mapValue != null) {
            if (mapValue == -1) {
                mapValue = waitFillingAndGet(product);
            }
            fromNumber = mapValue;
        } else {
            if (hashMap.putIfAbsent(product, (long) -1) != null) {
                fromNumber = waitFillingAndGet(product);
            } else {
                fromNumber = count.getAndIncrement();
                if (!product.isTerminal && (product.first >= fromNumber || product.second >= fromNumber))
                    throw new InvalidProductionRuleException(fromNumber, product);

                rules.put(fromNumber, product);
                statistics.put(fromNumber, calcStatistics(fromNumber));
                hashMap.put(product, fromNumber);
            }
        }
        return fromNumber;
    }

    private Long waitFillingAndGet(Product product) {
        while (true) {
            Long mapValue = hashMap.get(product);
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

    @Override
    public Product[] toNormalForm() {
        Integer[] indexes = new Integer[(int) count.get()];
        for (int i = 0; i < rules.size(); ++i)
            indexes[i] = i;
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(Integer index1, Integer index2) {
                long length1 = statistics.get((long) index1).length;
                long length2 = statistics.get((long) index2).length;
                if (length1 < length2)
                    return -1;
                if (length1 > length2)
                    return 1;
                return 0;
            }
        });
        Integer[] reverseIndexes = new Integer[rules.size()];
        for (int i = 0; i < rules.size(); ++i)
            reverseIndexes[indexes[i]] = i;
        Product[] sortedProducts = new Product[rules.size()];
        for (int i = 0; i < rules.size(); ++i) {
            Product oldProduct = rules.get((long) indexes[i]);
            sortedProducts[i] = oldProduct.isTerminal ? oldProduct : new Product(reverseIndexes[(int) oldProduct.first], reverseIndexes[(int) oldProduct.second]);
        }
        return sortedProducts;
    }

    @Override
    public String getProductString() {
        if (rules.size() == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        buildProductString(rules.size() - 1, builder);
        return builder.toString();
    }

    private void buildProductString(long ruleNumber, StringBuilder builder) {
        Product currentProduct = rules.get(ruleNumber);
        if (currentProduct.isTerminal)
            builder.append(currentProduct.symbol);
        else {
            buildProductString(currentProduct.first, builder);
            buildProductString(currentProduct.second, builder);
        }
    }

    @Override
    public SLPStatistics getStatistics() {
        if (rules.size() == 0)
            return new SLPStatistics(0, 0, 0);
        return statistics.get(count.get() - 1);
    }

    private SLPStatistics calcStatistics(long ruleNumber) {
        Product currentProduct = rules.get(ruleNumber);
        SLPStatistics result;
        if (currentProduct.isTerminal)
            result = new SLPStatistics(1, ruleNumber + 1, 1);
        else {
            SLPStatistics first = statistics.get(currentProduct.first);
            SLPStatistics second = statistics.get(currentProduct.second);
            result = new SLPStatistics(
                    first.length + second.length,
                    ruleNumber + 1,
                    Math.max(first.height, second.height) + 1);
        }
        return result;
    }
}
