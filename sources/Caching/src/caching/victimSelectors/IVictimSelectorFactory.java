package caching.victimSelectors;

public interface IVictimSelectorFactory
{
    IVictimSelector create(int cacheSize);
}
