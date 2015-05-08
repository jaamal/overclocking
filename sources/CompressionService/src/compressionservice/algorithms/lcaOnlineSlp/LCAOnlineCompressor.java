package compressionservice.algorithms.lcaOnlineSlp;

import avlTree.slpBuilders.SLPBuilder;
import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.Product;

public class LCAOnlineCompressor implements ILCAOnlineCompressor {
    public SLPBuilder buildSLP(IReadableCharArray text) {
        SLPBuilder slp = new SLPBuilder();
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

