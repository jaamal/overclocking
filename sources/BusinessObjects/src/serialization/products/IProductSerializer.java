package serialization.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import dataContracts.Product;

public interface IProductSerializer
{
    void serialize(OutputStream stream, Product[] products) throws IOException;
    Product[] deserialize(InputStream stream) throws IOException;
}
