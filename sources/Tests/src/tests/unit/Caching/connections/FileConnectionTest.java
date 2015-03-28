package tests.unit.Caching.connections;

import caching.connections.ConnectionNotOpenedYetException;
import caching.connections.FileConnection;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

import java.io.File;

public class FileConnectionTest extends UnitTestBase
{
    public final String fileName = "fileName";
    private FileConnection connection;

    @Override
    public void setUp()
    {
        super.setUp();
        connection = new FileConnection(new File(fileName));
        connection.open();
    }

    @Override
    public void tearDown()
    {
        connection.close();
        Assert.assertFalse(new File(fileName).delete());
        super.tearDown();
    }

    @Test
    public void testReadWrite()
    {
        connection.write(2, new byte[]{1, 2, 3});

        assertArrayEquals(new byte[]{0, 0, 1, 2}, connection.read(0, 4));

        assertArrayEquals(new byte[]{1, 2, 3}, connection.read(2, 3));

        connection.write(1, new byte[]{4, 5, 6, 7, 8, 9});

        assertArrayEquals(new byte[]{0, 4, 5, 6, 7, 8, 9, 0, 0}, connection.read(0, 9));
    }

    @Test(expected = ConnectionNotOpenedYetException.class)
    public void testReadNotOpened()
    {
        connection.close();
        connection.read(0, 1);
    }

    @Test(expected = ConnectionNotOpenedYetException.class)
    public void testWriteNotOpened()
    {
        connection.close();
        connection.write(0, new byte[]{0, 1});
    }

    private static void assertArrayEquals(byte[] expected, byte[] actual)
    {
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; ++i)
            Assert.assertEquals(expected[i], actual[i]);
    }
}

