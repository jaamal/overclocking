package tests.integration.Commons.files;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import commons.files.FileManager;
import commons.files.IFileManager;
import tests.integration.IntegrationTestBase;

public class FileManagerTest extends IntegrationTestBase
{
    private IFileManager fileManager;

    @Override
    public void tearDown() {
        if (fileManager != null)
            fileManager.deleteTempFiles();
        super.tearDown();
    }

    @Test
    public void testCreateTempFileWithNoSettings() throws IOException {
        fileManager = new FileManager(null);
        File file = fileManager.createTempFile();
        Assert.assertEquals("tmp", file.getParentFile().getName());
    }
    
    @Test
    public void testCreateTempFileWithSettings() throws IOException {
        fileManager = container.get(IFileManager.class);
        File file = fileManager.createTempFile();
        Assert.assertEquals("WorkingDirectory", file.getParentFile().getName());
    }
}
