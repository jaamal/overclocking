package avlTree.slpBuilders;

import dataContracts.LZFactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface IConcurrencyAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics);
    ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics, ConcurrentAvlBuilderStopwatches stopwatches);
}
