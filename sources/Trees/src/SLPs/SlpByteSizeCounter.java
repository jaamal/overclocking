package SLPs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import avlTree.slpBuilders.ISLPBuilder;
import serialization.products.IProductsSerializer;
import dataContracts.Product;

public class SlpByteSizeCounter {
    private final IProductsSerializer slpSerializer;

    public SlpByteSizeCounter(IProductsSerializer slpSerializer) {
        this.slpSerializer = slpSerializer;
    }

    public long getSlpByteSize(ISLPBuilder slpBuilder) {
        Product[] products = slpBuilder.toNormalForm();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            slpSerializer.serialize(stream, products);
            return stream.size();
        } catch (IOException e) {
            throw new RuntimeException("Throws exception while serializing SLP", e);
        }
    }
}
