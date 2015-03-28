package tests.unit.Commons;

import commons.files.FileManager;
import commons.files.IFileManager;
import compressingCore.dataAccess.FileLongArray;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.junit.Assert.assertEquals;

public class FileLongArrayTest extends UnitTestBase
{
    @Test
    public void testFileLongArray()
    {
        IFileManager fileManager = new FileManager();
        try(FileLongArray longArray = new FileLongArray(fileManager, "..\\\\WorkingDirectory", 10 * 1024);)
        {
            for (int i = 0; i < longArray.size(); i++)
            {
                longArray.set(i, i);
            }
            for (int i = 0; i < longArray.size(); i++)
            {
                assertEquals(i, longArray.get(i));
            }
        }
    }
}
