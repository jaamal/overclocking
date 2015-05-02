package cartesianTree.slpBuilders;

import avlTree.slpBuilders.ISLPBuilder;
import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;

public interface ICartesianSlpTreeBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, IStatistics statistics);
}
