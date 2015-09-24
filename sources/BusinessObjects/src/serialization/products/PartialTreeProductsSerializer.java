package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import dataContracts.Product;
import productEnumerator.ProductEnumerator;
import serialization.primitives.IIntArraySerializer;
import serialization.primitives.IntArraySerializer;

public class PartialTreeProductsSerializer implements IProductSerializationHeuristic {
    private final IIntArraySerializer intArraySerializer;
    private static final char MAX_SYMBOL = 256;

    public PartialTreeProductsSerializer() {
        this(new IntArraySerializer());
    }

    public static PartialTreeProductsSerializer create(IIntArraySerializer intArraySerializer) {
        return new PartialTreeProductsSerializer(intArraySerializer);
    }
    
    private PartialTreeProductsSerializer(IIntArraySerializer intArraySerializer) {
        this.intArraySerializer = intArraySerializer;
    }

    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException {
        if (products.length == 0)
            throw new RuntimeException("Fail to serialize empty array of products.");
        
        int[] values = toPartialTreeRepresentation(products);
        intArraySerializer.serialize(stream, values);
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException {
        int[] values = intArraySerializer.deserialize(stream);
        return fromPartialTreeRepresentation(values);
    }
    
    @Override
    public byte getSerializerId()
    {
        return 4;
    }

    private static Product[] fromPartialTreeRepresentation(int[] values) {
        ArrayList<Integer> numbers = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        ProductEnumerator productEnumerator = new ProductEnumerator();
        for (int i = 0; i < values.length; ++i) {
            if (values[i] < 0) {
                int value = -values[i];
                if (value < MAX_SYMBOL){
                    int number = (int) productEnumerator.append(new Product((char) value));
                    stack.push(number);
                }
                else {
                    stack.push(numbers.get(value - MAX_SYMBOL));
                }
            }
            else {
                int second = stack.pop();
                int first = stack.pop();
                int number = (int) productEnumerator.append(new Product(first, second));
                numbers.add(number);
                stack.push(number);
            }
        }
        return productEnumerator.toSLPModel().toNormalForm();
    }

    private static int[] toPartialTreeRepresentation(Product[] products) {
        HashMap<Integer, Integer> number = new HashMap<>();
        ArrayList<Integer> values = new ArrayList<>();
        buildRepresentation(products.length - 1, products, number, values);
        
        int[] result = new int[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    private static void buildRepresentation(int current, Product[] products, HashMap<Integer, Integer> number, ArrayList<Integer> values) {
        Product product = products[current];
        if (product.isTerminal) {
            if (product.symbol >= MAX_SYMBOL)
                throw new RuntimeException(String.format("Assertion failed: symbol(%c) is not less than MAX_SYMBOL", product.symbol));
            values.add(-((int) product.symbol));
        } else if (number.containsKey(current)) {
            values.add(-(number.get(current) + MAX_SYMBOL));
        } else {
            buildRepresentation((int) product.first, products, number, values);
            buildRepresentation((int) product.second, products, number, values);
            number.put(current, number.size());
            values.add(current);
        }
    }
}
