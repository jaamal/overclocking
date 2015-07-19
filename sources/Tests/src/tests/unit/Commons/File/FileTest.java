package tests.unit.Commons.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import helpers.TestHelpers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

import tests.unit.UnitTestBase;

import commons.files.FileImpl;
import commons.files.IFile;

public class FileTest extends UnitTestBase
{
    @Test
    public void testTemporaryFile() throws IOException
    {
        String fileName = "zzz.qxx";
        try (FileImpl temporaryFile = new FileImpl(fileName)){
            temporaryFile.append(new byte[]{1, 2, 3});
            temporaryFile.append(new byte[]{4, 5, 6});
            temporaryFile.append(new byte[]{7, 8, 9});
            
            testFileContent(fileName, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
            
            testFileBatch(temporaryFile, 0, 9, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
            testFileBatch(temporaryFile, 1, 9, new byte[]{2, 3, 4, 5, 6, 7, 8, 9});
            testFileBatch(temporaryFile, 5, 2, new byte[]{6, 7});

            assertEquals(-1, temporaryFile.read(9, new byte[10]));
        }
        finally {
            new FileImpl(fileName).delete();
            assertFalse(new File(fileName).exists());
        }
    }

    @Test
    public void testTemporaryFileHard() throws IOException
    {
        String fileName = "qxx.zzz";
        try (FileImpl temporaryFile = new FileImpl(fileName)){
            byte[] hugeArray = new byte[1024 * 1024];
            for (int i = 0; i < 1024; i++)
            {
                byte[] small = TestHelpers.genBytes(1024);
                System.arraycopy(small, 0, hugeArray, i * 1024, small.length);
                temporaryFile.append(small);
            }
            testFileContent(fileName, hugeArray);
            testFileBatch(temporaryFile, 0, hugeArray.length + 39120891, hugeArray);
        }
        finally {
            new FileImpl(fileName).delete();
            assertFalse(new File(fileName).exists());
        }
    }

    private static void testFileBatch(IFile file, long offset, int maxLength, byte[] expected) throws IOException
    {
        byte[] buffer = new byte[maxLength];
        int actual = file.read(offset, buffer);
        assertEquals(expected.length, actual);
        for (int i = 0; i < expected.length; i++)
            assertEquals(expected[i], buffer[i]);
    }

    private static void testFileContent(String fileName, byte[] expected) throws IOException
    {
        byte[] actual = new byte[expected.length];
        try (RandomAccessFile file = new RandomAccessFile(fileName, "r"))
        {
            file.read(actual);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        assertArrayEquals(expected, actual);
    }
}
