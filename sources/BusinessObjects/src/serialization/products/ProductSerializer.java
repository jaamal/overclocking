package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import dataContracts.Product;

public class ProductSerializer implements IProductSerializer
{
    private IProductSerializationHeuristic[] heuristics;

    public ProductSerializer(IProductSerializationHeuristic[] heuristics) {
        this.heuristics = heuristics;
    }
    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException
    {
        long minSerializedSize = Long.MAX_VALUE;
        IProductSerializationHeuristic optimalHeuristic = null;
        for (int i = 0; i < heuristics.length; i++) {
            long currentSerializedSize;
            try (LengthCalculatorOutputStream fakeOutputStream = new LengthCalculatorOutputStream()) {
                heuristics[i].serialize(fakeOutputStream, products);
                currentSerializedSize = fakeOutputStream.getLengthInBytes();
            }
            if (currentSerializedSize < minSerializedSize) {
                minSerializedSize = currentSerializedSize;
                optimalHeuristic = heuristics[i];
            }
        }
        
        stream.write(new byte[] { optimalHeuristic.getSerializerId() });
        optimalHeuristic.serialize(stream, products);
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException
    {
        byte heuristicId = (byte) stream.read();
        if (heuristicId == -1)
            throw new IOException("Try to read from empty stream!");

        IProductSerializationHeuristic heuristic = null;
        for (int i = 0; i < heuristics.length; i++) {
            if (heuristics[i].getSerializerId() == heuristicId) {
                heuristic = heuristics[i];
                break;
            }
        }
        
        if (heuristic == null)
            throw new RuntimeException(String.format("Fail to find heuristic with id %s.", heuristicId));

        return heuristic.deserialize(stream);
    }
    
    @Override
    public long calcSizeInBytes(Product[] products)
    {
        try {
            IProductSerializationHeuristic optimalHeuristic = null;
            return chooseOptimalHeuristic(products, optimalHeuristic);
        }
        catch (IOException ex) {
            throw new RuntimeException("Fail to calculate products size.", ex);
        }
    }
    
    private long chooseOptimalHeuristic(Product[] products, IProductSerializationHeuristic optimalHeuristic) throws IOException {
        long minSerializedSize = Long.MAX_VALUE;
        for (int i = 0; i < heuristics.length; i++) {
            long currentSerializedSize;
            try (LengthCalculatorOutputStream fakeOutputStream = new LengthCalculatorOutputStream()) {
                heuristics[i].serialize(fakeOutputStream, products);
                currentSerializedSize = fakeOutputStream.getLengthInBytes();
            }
            if (currentSerializedSize < minSerializedSize) {
                minSerializedSize = currentSerializedSize;
                optimalHeuristic = heuristics[i];
            }
        }
        return minSerializedSize;
    }
}
