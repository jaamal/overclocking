package dataContracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import serialization.products.IProductsSerializer;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;

//TODO: test this class
//TODO: verify name of class
public class SLPModel
{
    private final AbstractMap<Long, Product> numbersIndex;
    private final HashMap<Long, SLPStatistics> statisticsIndex;
    
    public SLPModel(AbstractMap<Long, Product> numbersIndex) {
        this.numbersIndex = numbersIndex;
        this.statisticsIndex = new HashMap<Long, SLPStatistics>(numbersIndex.size());
    }

    public Product[] toNormalForm() {
        calcStats();
        
        int size = numbersIndex.size();
        Integer[] indexes = new Integer[size];
        for (int i = 0; i < size; ++i)
            indexes[i] = i;
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(Integer index1, Integer index2) {
                long length1 = statisticsIndex.get((long) index1).length;
                long length2 = statisticsIndex.get((long) index2).length;
                if (length1 < length2)
                    return -1;
                if (length1 > length2)
                    return 1;
                return 0;
            }
        });
        Integer[] reverseIndexes = new Integer[size];
        for (int i = 0; i < size; ++i)
            reverseIndexes[indexes[i]] = i;
        Product[] sortedProducts = new Product[size];
        for (int i = 0; i < size; ++i) {
            Product oldProduct = numbersIndex.get((long) indexes[i]);
            sortedProducts[i] = oldProduct.isTerminal ? oldProduct : new Product(reverseIndexes[(int) oldProduct.first], reverseIndexes[(int) oldProduct.second]);
        }
        return sortedProducts;
    }
    
    public void appendStats(IStatistics to, IProductsSerializer productsSerializer) {
        SLPStatistics stats = calcStats();
        to.putParam(StatisticKeys.SlpWidth, stats.length);
        to.putParam(StatisticKeys.SlpHeight, stats.height);
        to.putParam(StatisticKeys.SlpCountRules, stats.countRules);
        
        Product[] products = toNormalForm();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            productsSerializer.serialize(stream, products);
            to.putParam(StatisticKeys.SlpByteSize, stream.size());
        } catch (IOException e) {
            throw new RuntimeException("Fail to serialize products.", e);
        }
    }
    
    public SLPStatistics calcStats() {
        if (statisticsIndex.isEmpty()) {
            for (Long number : numbersIndex.keySet()) {
                calcStats(number);
            }
        }
        
        return numbersIndex.size() == 0 
                ? new SLPStatistics(0, 0, 0)
                : statisticsIndex.get((long) (numbersIndex.size() - 1));
    }
    
    private void calcStats(Long ruleNumber)
    {
        if (statisticsIndex.containsKey(ruleNumber))
            return;
        
        Product product = numbersIndex.get(ruleNumber);
        if (product.isTerminal)
            statisticsIndex.put(ruleNumber, new SLPStatistics(1, ruleNumber + 1, 1));
        else
        {
            SLPStatistics first = statisticsIndex.get(product.first);
            if (first == null) {
                calcStats(product.first);
                first = statisticsIndex.get(product.first);
            }
            SLPStatistics second = statisticsIndex.get(product.second);
            if (second == null) {
                calcStats(product.second);
                second = statisticsIndex.get(product.second);
            }
            statisticsIndex.put(ruleNumber, new SLPStatistics(first.length + second.length, ruleNumber + 1,
                                Math.max(first.height, second.height) + 1));
        }
    }
    
    @Override
    public String toString() {
        if (numbersIndex.size() == 0)
            return "";
        StringBuilder builder = new StringBuilder();
        toString(builder, numbersIndex.size() - 1);
        return builder.toString();
    }
    
    private void toString(StringBuilder builder, long ruleNumber)
    {
        Product currentProduct = numbersIndex.get(ruleNumber);
        if (currentProduct.isTerminal)
            builder.append(currentProduct.symbol);
        else
        {
            toString(builder, currentProduct.first);
            toString(builder, currentProduct.second);
        }
    }
}
