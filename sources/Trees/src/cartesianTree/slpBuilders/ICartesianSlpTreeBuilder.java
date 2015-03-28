package cartesianTree.slpBuilders;

import avlTree.slpBuilders.ISLPBuilder;
import dataContracts.LZFactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface ICartesianSlpTreeBuilder
{
    ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics);
}
