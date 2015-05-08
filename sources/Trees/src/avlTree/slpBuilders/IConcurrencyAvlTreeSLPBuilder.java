package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;

public interface IConcurrencyAvlTreeSLPBuilder
{
    SLPModel buildSlp(FactorDef[] factors, IStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches);
}
