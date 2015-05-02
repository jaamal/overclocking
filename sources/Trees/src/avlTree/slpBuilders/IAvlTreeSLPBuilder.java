package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;

public interface IAvlTreeSLPBuilder
{
    ISLPBuilder buildSlp(FactorDef[] factors, IStatistics statistics);
}
