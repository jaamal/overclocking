package tests.unit.Commons.File;

import commons.files.FileImpl;
import commons.files.IFile;

import org.junit.Test;

import tests.unit.UnitTestBase;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import static org.junit.Assert.*;

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
            check(fileName, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
            checkRead(temporaryFile, 0, 9, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, 9);
            checkRead(temporaryFile, 1, 9, new byte[]{2, 3, 4, 5, 6, 7, 8, 9}, 8);
            checkRead(temporaryFile, 5, 2, new byte[]{6, 7}, 2);
            checkRead(temporaryFile, 9, 10, new byte[0], -1);
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
        FileImpl temporaryFile = new FileImpl(fileName);
        byte[] hugeArray = new byte[1024 * 1024];
        Random random = new Random();
        for (int i = 0; i < 1024; i++)
        {
            byte[] small = new byte[1024];
            random.nextBytes(small);
            System.arraycopy(small, 0, hugeArray, i * 1024, small.length);
            temporaryFile.append(small);
        }
        check(fileName, hugeArray);
        checkRead(temporaryFile, 0, hugeArray.length + 39120891, hugeArray, hugeArray.length);
        temporaryFile.delete();
        assertFalse(new File(fileName).exists());
    }

    private static void checkRead(IFile file, long offset, int maxLength, byte[] expected, int expectedResult) throws IOException
    {
        byte[] buffer = new byte[maxLength];
        int actual = file.read(offset, buffer);
        assertEquals(expectedResult, actual);
        for (int i = 0; i < expectedResult; i++)
            assertEquals(expected[i], buffer[i]);
    }

    private static void check(String fileName, byte[] expected) throws IOException
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
