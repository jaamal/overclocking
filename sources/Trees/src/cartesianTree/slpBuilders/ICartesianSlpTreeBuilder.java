package cartesianTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;

public interface ICartesianSlpTreeBuilder
{
    SLPModel buildSlp(FactorDef[] factors, IStatistics statistics);
}
