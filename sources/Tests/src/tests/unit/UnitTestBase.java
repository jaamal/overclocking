package tests.unit;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import tests.TestBase;

public abstract class UnitTestBase extends TestBase
{
    private List<Object> mocks;

    protected <T> T newMock(java.lang.Class<T> toMock)
    {
        T mock = createMock(toMock);
        mocks.add(mock);
        return mock;
    }

    @Override
    public void setUp()
    {
        super.setUp();
        mocks = new ArrayList<Object>();
    }

    protected void replayAll()
    {
        for (Object mock : mocks)
            replay(mock);
    }
    
    protected void resetAll()
    {
        for (Object mock : mocks)
            reset(mock);
    }

    @Override
    public void tearDown()
    {
        for (Object mock : mocks)
            verify(mock);
    }
}
