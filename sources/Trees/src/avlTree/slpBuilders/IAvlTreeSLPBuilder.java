package avlTree.slpBuilders;

import dataContracts.LZFactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface IAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(LZFactorDef[] factors, ICompressionStatistics statistics);
}
