package tests;

import org.junit.After;
import org.junit.Before;

public abstract class TestBase
{
    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        System.gc();
    }
}
