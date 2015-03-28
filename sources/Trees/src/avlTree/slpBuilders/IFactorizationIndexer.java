package avlTree.slpBuilders;

import java.util.List;

import dataContracts.LZFactorDef;

public interface IFactorizationIndexer {
	List<List<Integer>> index(LZFactorDef[] factors);
}
