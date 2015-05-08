package SLPs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import serialization.products.IProductsSerializer;
import dataContracts.Product;
import dataContracts.SLPModel;

public class SlpByteSizeCounter {
    private final IProductsSerializer slpSerializer;

    public SlpByteSizeCounter(IProductsSerializer slpSerializer) {
        this.slpSerializer = slpSerializer;
    }

    public long getSlpByteSize(SLPModel slpModel) {
        Product[] products = slpModel.toNormalForm();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            slpSerializer.serialize(stream, products);
            return stream.size();
        } catch (IOException e) {
            throw new RuntimeException("Throws exception while serializing SLP", e);
        }
    }
}
