package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface IConcurrencyAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, ICompressionStatistics statistics);
    ISLPBuilder buildSlp(FactorDef[] factors, ICompressionStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches);
}
