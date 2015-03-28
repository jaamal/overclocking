package patternMatching.fcpm.localsearch;

public class LocalSearchResultFactory implements ILocalSearchResultFactory {
    @Override
    public LocalSearchResult create() {
        return new LocalSearchResult();
    }
}
