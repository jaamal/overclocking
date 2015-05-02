package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;

public interface IConcurrencyAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, IStatistics statistics);
    ISLPBuilder buildSlp(FactorDef[] factors, IStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches);
}
