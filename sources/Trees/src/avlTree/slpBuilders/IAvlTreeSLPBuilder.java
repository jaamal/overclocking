package avlTree.slpBuilders;

import dataContracts.FactorDef;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;

public interface IAvlTreeSLPBuilder
{
    SLPModel buildSlp(FactorDef[] factors, IStatistics statistics);
}
