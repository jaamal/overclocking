package avlTree.slpBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import dataContracts.Product;
import dataContracts.SLPStatistics;

public class SLPBuilder implements ISLPBuilder
{
    public final ArrayList<Product> rules;
    public final ArrayList<SLPStatistics> statistics;
    public final HashMap<Product, Long> hashMap;

    public SLPBuilder()
    {
        hashMap = new HashMap<>();
        rules = new ArrayList<>();
        statistics = new ArrayList<>();
    }

    public long addRule(Product product)
    {
        long fromNumber;
        Long mapValue = hashMap.get(product);
        if (mapValue != null) {
            fromNumber = mapValue;
        } else {
            fromNumber = rules.size();
            if (!product.isTerminal && (product.first >= fromNumber || product.second >= fromNumber))
                throw new InvalidProductionRuleException(fromNumber, product);

            hashMap.put(product, fromNumber);
            rules.add(product);
            calcLastStatistics();
        }
        return fromNumber;
    }

    @Override
    public Product[] toNormalForm() {
        Integer[] indexes = new Integer[rules.size()];
        for (int i = 0; i < rules.size(); ++i)
            indexes[i] = i;
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(Integer index1, Integer index2) {
                long length1 = statistics.get(index1).length;
                long length2 = statistics.get(index2).length;
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
            Product oldProduct = rules.get(indexes[i]);
            sortedProducts[i] = oldProduct.isTerminal ? oldProduct : new Product(reverseIndexes[(int)oldProduct.first], reverseIndexes[(int)oldProduct.second]);
        }
        return sortedProducts;
    }

    @Override
    public String getProductString()
    {
        if (rules.size() == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        buildProductString(rules.size() - 1, builder);
        return builder.toString();
    }

    private void buildProductString(long ruleNumber, StringBuilder builder)
    {
        Product currentProduct = rules.get((int) ruleNumber);
        if (currentProduct.isTerminal)
            builder.append(currentProduct.symbol);
        else
        {
            buildProductString(currentProduct.first, builder);
            buildProductString(currentProduct.second, builder);
        }
    }

    @Override
    public SLPStatistics getStatistics()
    {
        if (rules.size() == 0)
            return new SLPStatistics(0, 0, 0);
        return statistics.get(rules.size() - 1);
    }

    private void calcLastStatistics()
    {
        long ruleNumber = rules.size() - 1;
        Product currentProduct = rules.get((int) ruleNumber);
        SLPStatistics result;
        if (currentProduct.isTerminal)
            result = new SLPStatistics(1, ruleNumber + 1, 1);
        else
        {
            SLPStatistics first = statistics.get((int) currentProduct.first);
            SLPStatistics second = statistics.get((int) currentProduct.second);
            result = new SLPStatistics(
                    first.length + second.length,
                    ruleNumber + 1,
                    Math.max(first.height, second.height) + 1);
        }
        statistics.add(result);
    }
}
