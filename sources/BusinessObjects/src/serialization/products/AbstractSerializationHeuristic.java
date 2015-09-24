package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
import dataContracts.Product;

public abstract class AbstractSerializationHeuristic implements IProductSerializationHeuristic {
    
    protected abstract void serializeProduct(OutputStream stream, int index, Product product) throws IOException;
    protected abstract Product deserializeProduct(InputStream stream, int index) throws IOException;
    
    @Override
    public void serialize(OutputStream stream, Product[] products) throws IOException {
        StreamHelpers.writeInt(stream, products.length);
        for (int i = 0; i < products.length; i++)
            serializeProduct(stream, i, products[i]);
    }

    @Override
    public Product[] deserialize(InputStream stream) throws IOException {
        int productsCount = StreamHelpers.readInt(stream);
        Product[] products = new Product[productsCount];
        for (int i = 0; i < productsCount; ++i)
            products[i] = deserializeProduct(stream, i);
        return products;
    }
}
