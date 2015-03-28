package caching.victimSelectors;

public interface IVictimSelector
{
    void fixUsage(int number);
    int selectVictimAndFixUsage();
}
