package tests.unit.Caching.victimSelectors;

import caching.doubleLinkedList.IList;
import caching.doubleLinkedList.IListFactory;
import caching.victimSelectors.LRUVictimSelector;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.expect;

public class LRUVictimSelectorTest extends UnitTestBase
{
    private IList list;
    private LRUVictimSelector selector;

    @Override
    public void setUp()
    {
        super.setUp();
        IListFactory listFactory = newMock(IListFactory.class);
        list = newMock(IList.class);
        int cacheSize = 10;
        expect(listFactory.create(cacheSize)).andReturn(list);
        for (int i = 0; i < cacheSize; ++i)
            list.appendToBegin(i);
        replayAll();
        selector = new LRUVictimSelector(listFactory, cacheSize);
        resetAll();
    }

    @Test
    public void testFixUsage()
    {
        int number = 42;
        list.moveToBegin(number);
        replayAll();
        selector.fixUsage(number);
    }

    @Test
    public void testSelectVictim()
    {
        int victim = 33;
        expect(list.getLastKey()).andReturn(victim);
        list.moveToBegin(victim);
        replayAll();
        Assert.assertEquals(selector.selectVictimAndFixUsage(), victim);
    }
}
