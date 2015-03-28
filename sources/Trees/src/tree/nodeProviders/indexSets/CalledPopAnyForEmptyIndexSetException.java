package tree.nodeProviders.indexSets;

public class CalledPopAnyForEmptyIndexSetException extends RuntimeException {

	private static final long serialVersionUID = 320065118081374842L;

	public CalledPopAnyForEmptyIndexSetException() {
		super("Called popAny() for empty IIndexSet.");
	}
}
