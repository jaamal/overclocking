package compressionservice.algorithms.lcaOnlineSlp;

public class SymbolPairs {

    public static boolean isNeedCompressPair(ILongArray array, int index) {
        if (isRepetitivePair(array, index))
            return true;
        if (isRepetitivePair(array, index + 1))
            return false;
        if (isRepetitivePair(array, index + 2))
            return true;
        if (isMinimalPair(array, index) || isMaximalPair(array, index))
            return true;
        if (isMinimalPair(array, index + 1) || isMaximalPair(array, index + 1))
            return false;
        return true;
    }

    private static boolean isRepetitivePair(ILongArray array, int index) {
        return array.get(index) == array.get(index + 1);
    }

    private static boolean isMinimalPair(ILongArray array, int index) {
        return array.get(index) < array.get(index - 1) && array.get(index) < array.get(index + 1);
    }

    private static boolean isMaximalPair(ILongArray array, int index) {
        long i = array.get(index - 1);
        long j = array.get(index);
        long k = array.get(index + 1);
        long l = array.get(index + 2);
        if (!(i < j && j < k && k < l) && !(i > j && j > k && k > l))
            return false;

        int lca1 = getLcaDepth(i, j);
        int lca2 = getLcaDepth(j, k);
        int lca3 = getLcaDepth(k, l);
        return lca2 > lca1 && lca2 > lca3;
    }

    private static int getLcaDepth(long a, long b) {
        long x = (a + 1) ^ (b + 1);
        int i = 0;
        while ((1 << i) < x)
            ++i;
        return 64 - i;
    }
}
