package avlTree.slpBuilders;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import dataContracts.LZFactorDef;

public class FactorizationIndexer implements IFactorizationIndexer {
	private static Logger log = LogManager.getLogger(FactorizationIndexer.class);
	
	@Override
	public List<List<Integer>> index(LZFactorDef[] factors) {
		long starMs = System.currentTimeMillis();
		
        List<Integer> level = new ArrayList<>(factors.length);
        List<List<Integer>> result = new ArrayList<>();
        long currentOffset = 0;
        for (int i = 0; i < factors.length; ++i) {
        	factors[i].offset = currentOffset;
            currentOffset += factors[i].isTerminal ? 1 : factors[i].length;

            int currentLevel;
            if (factors[i].isTerminal) {
                currentLevel = 0;
            } else {
                int index = findFirstFactorUpTo(factors, i);
                int maxLevel = -1;
                while (factors[i].offset != null && factors[index].offset < factors[i].beginPosition + (factors[i].isTerminal ? 1 : factors[i].length)) {
                    maxLevel = Math.max(maxLevel, level.get(index));
                    index++;
                }
                currentLevel = maxLevel + 1;
            }
            if (result.size() == currentLevel)
                result.add(new ArrayList<Integer>());
            result.get(currentLevel).add(i);
            level.add(currentLevel);
        }
        
        long endMs = System.currentTimeMillis();
        log.info(String.format("factorization index %d ms.", endMs - starMs));
        
        return result;
	}

    private static int findFirstFactorUpTo(LZFactorDef[] factors, int upToFactorIndex) {
        int left = 0;
        int right = upToFactorIndex;
        long beginPosition = factors[upToFactorIndex].beginPosition;
        while (left + 1 < right) {
            int middle = (left + right + 1) / 2;
            if (factors[middle].offset <= beginPosition)
                left = middle;
            else
                right = middle;
        }
        return left;
    }

}
