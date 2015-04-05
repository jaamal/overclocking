package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.statistics.ICompressionStatistics;

public interface IAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, ICompressionStatistics statistics);
}
