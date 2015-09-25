package compressionservice.algorithms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import dataContracts.AlgorithmType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.Statistics;
import serialization.products.IProductSerializer;

public class AvlSlpAlgorithm extends Algorithm implements ISlpCompressionAlgorithm {

    private final IAvlTreeSLPBuilder avlTreeSLPBuilder;
    private final IResourceProvider resourceProvider;
    private IProductSerializer productSerializer;
    private final String sourceId;
    private IStatistics statistics;
    SLPModel slpModel;

    public AvlSlpAlgorithm(
            IAvlTreeSLPBuilder avlTreeSLPBuilder,
            IResourceProvider resourceProvider,
            IProductSerializer productSerializer,
            String sourceId)
    {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.resourceProvider = resourceProvider;
        this.productSerializer = productSerializer;
        this.sourceId = sourceId;
    }

    @Override
    protected void runInternal()
    {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        statistics = new Statistics();
        slpModel = avlTreeSLPBuilder.buildSlp(factorization, statistics);
    }
    
    @Override
    public IStatistics getStats()
    {
        checkIsFinished();
        return statistics;
    }

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.avlSlp;
    }

    @Override
    public byte[] getCompressedRepresentation()
    {
        checkIsFinished();
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            productSerializer.serialize(stream, getSlp());
            return stream.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException("Fail to build slp compressed representation", e);
        }
    }

    @Override
    public Product[] getSlp()
    {
        checkIsFinished();
        return slpModel.toNormalForm();
    }
}
