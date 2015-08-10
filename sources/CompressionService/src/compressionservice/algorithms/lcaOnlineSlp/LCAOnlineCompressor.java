package compressionservice.algorithms.lcaOnlineSlp;

import data.charArray.IReadableCharArray;
import dataContracts.Product;
import productEnumerator.ProductEnumerator;

public class LCAOnlineCompressor implements ILCAOnlineCompressor {
    public ProductEnumerator buildSLP(IReadableCharArray text) {
        ProductEnumerator slp = new ProductEnumerator();
        ICompressingQueue queue = new CompressingQueue(slp);
        for (long i = 0; i < text.length(); i += batchSize) {
            IReadableCharArray batch = text.subArray(i, Math.min(text.length(), i + batchSize));
            for (int j = 0; j < batch.length(); ++j) {
                char symbol = batch.get(j);
                long fromNumber = slp.append(new Product(symbol));
                queue.insertSymbol(fromNumber);
            }
        }
        queue.postProcessingRemain();
        return slp;
    }

    private final long batchSize = 4096;
}

