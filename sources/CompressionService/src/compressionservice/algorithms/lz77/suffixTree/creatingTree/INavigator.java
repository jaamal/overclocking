package compressionservice.algorithms.lz77.suffixTree.creatingTree;

public interface INavigator
{
    public IInsertPlace getPath(IBeginPlace beginPlace, int beginPosition, int endPosition);
}
