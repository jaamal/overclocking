package tree.nodeProviders.indexSets;

public interface IIndexSet {
	void add(long index);
	long tryPopAny(); // returns -1, if unsuccessfully
    long count();
	void close();
}
