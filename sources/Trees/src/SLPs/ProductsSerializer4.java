package SLPs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import serialization.primitives.BooleanArraySerializer;
import serialization.primitives.IBooleanArraySerializer;
import serialization.primitives.IIntArraySerializer;
import serialization.primitives.IntArraySerializer;
import serialization.products.IProductsSerializer;
import avlTree.slpBuilders.SLPBuilder;
import dataContracts.Product;

//TODO: it is very strange that serialization depends on SLPBuilder.
public class ProductsSerializer4 implements IProductsSerializer {
    private final IBooleanArraySerializer booleanArraySerializer;
    private final IIntArraySerializer intArraySerializer;
    private static final char MAX_SYMBOL = 256;

    public ProductsSerializer4() {
        this(new IntArraySerializer());
    }

    public ProductsSerializer4(IIntArraySerializer intArraySerializer) {
        booleanArraySerializer = new BooleanArraySerializer();
        this.intArraySerializer = intArraySerializer;
    }

    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException {
        if (products.length == 0)
            throw new RuntimeException("Fail to serialize empty array of products.");
        
        PartialTreeRepresentation representation = toPartialTreeRepresentation(products);
        booleanArraySerializer.serialize(stream, representation.isLeaf);
        intArraySerializer.serialize(stream, representation.values);
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException {
        boolean[] isLeaf = booleanArraySerializer.deserialize(stream);
        int[] values = intArraySerializer.deserialize(stream);
        PartialTreeRepresentation representation = new PartialTreeRepresentation(isLeaf, values);
        return fromPartialTreeRepresentation(representation);
    }

    private static Product[] fromPartialTreeRepresentation(PartialTreeRepresentation representation) {
        ArrayList<Integer> number = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        SLPBuilder slpBuilder = new SLPBuilder();
        for (int i = 0, currentValue = 0; i < representation.isLeaf.length; ++i) {
            if (representation.isLeaf[i])
                stack.push(findNonTerminalNumber(representation.values[currentValue++], number, slpBuilder));
            else {
                int second = stack.pop();
                int first = stack.pop();
                int fromNumber = (int) slpBuilder.addRule(new Product(first, second));
                number.add(fromNumber);
                stack.push(fromNumber);
            }
        }
        return slpBuilder.toNormalForm();
    }

    private static int findNonTerminalNumber(int encodedNumber, ArrayList<Integer> number, SLPBuilder slpBuilder) {
        if (encodedNumber < MAX_SYMBOL) {
            return (int) slpBuilder.addRule(new Product((char) encodedNumber));
        }
        return number.get(encodedNumber - MAX_SYMBOL);
    }

    private static PartialTreeRepresentation toPartialTreeRepresentation(Product[] products) {
        HashMap<Integer, Integer> number = new HashMap<>();
        ArrayList<Boolean> isLeaf = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        buildRepresentation(products.length - 1, products, number, isLeaf, values);
        return new PartialTreeRepresentation(isLeaf, values);
    }

    private static void buildRepresentation(int current, Product[] products, HashMap<Integer, Integer> number, ArrayList<Boolean> isLeaf, ArrayList<Integer> values) {
        Product product = products[current];
        if (product.isTerminal) {
            if (product.symbol >= MAX_SYMBOL)
                throw new RuntimeException(String.format("Assertion failed: symbol(%c) is not less than MAX_SYMBOL", product.symbol));
            values.add((int) product.symbol);
            isLeaf.add(true);
        } else if (number.containsKey(current)) {
            values.add(number.get(current) + MAX_SYMBOL);
            isLeaf.add(true);
        } else {
            buildRepresentation((int) product.first, products, number, isLeaf, values);
            buildRepresentation((int) product.second, products, number, isLeaf, values);
            int currentNumber = number.size();
            number.put(current, currentNumber);
            isLeaf.add(false);
        }
    }

    private static class PartialTreeRepresentation {
        public boolean[] isLeaf;
        public int[] values;

        public PartialTreeRepresentation(boolean[] isLeaf, int[] values) {
            this.isLeaf = isLeaf;
            this.values = values;
        }

        public PartialTreeRepresentation(ArrayList<Boolean> isLeaf, ArrayList<Integer> values) {
            this.isLeaf = new boolean[isLeaf.size()];
            for (int i = 0; i < this.isLeaf.length; ++i)
                this.isLeaf[i] = isLeaf.get(i);
            this.values = new int[values.size()];
            for (int i = 0; i < this.values.length; ++i)
                this.values[i] = values.get(i);
        }
    }
}
