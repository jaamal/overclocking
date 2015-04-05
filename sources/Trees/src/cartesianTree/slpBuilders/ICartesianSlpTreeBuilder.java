package cartesianTree.slpBuilders;

import avlTree.slpBuilders.ISLPBuilder;
import dataContracts.FactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface ICartesianSlpTreeBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, ICompressionStatistics statistics);
}
