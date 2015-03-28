package avlTree.slpBuilders;


public interface IParallelExecutor {
	void append(Runnable task);
	void await();
}
