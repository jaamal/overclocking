package tests.integration.BusinessObjects;

import java.io.IOException;

import helpers.TestHelpers;

import org.junit.Assert;
import org.junit.Test;

import commons.files.IFile;
import commons.files.IFileManager;
import data.IDataFactory;
import data.charArray.IReadableCharArray;
import tests.integration.IntegrationTestBase;

public class CharArrayTest extends IntegrationTestBase
{
    @Test
    public void testSaveToFileHugeString() throws IOException {
        String mbText = TestHelpers.genString(1024 * 1024);
        try (IReadableCharArray readableCharArray = container.get(IDataFactory.class).createCharArray(mbText.toCharArray());
             IFile textFile = container.get(IFileManager.class).createTempFile())
        {
            readableCharArray.saveToFile(textFile);
            
            String actual = "";
            byte[] buffer = new byte[128 * 1024];
            long position = 0;
            while (position < mbText.length())
            {
                int count = textFile.read(position, buffer);
                if (count == buffer.length) {
                    actual += new String(buffer);
                }
                else {
                    for (int i = 0; i < count; i++)
                        actual += (char) buffer[i];
                }
                position += count;
            }
            
            Assert.assertArrayEquals(mbText.toCharArray(), actual.toCharArray());
            textFile.delete();
        }
    }
}
