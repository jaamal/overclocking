package tests.integration.Commons.files;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import tests.integration.IntegrationTestBase;

import commons.files.FileManager;
import commons.files.IFile;
import commons.files.IFileManager;

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
        try (IFile file = fileManager.createTempFile()) {
            String pathStr = file.getPathStr();
            Assert.assertTrue(pathStr.contains("/tmp/"));
        }
    }
    
    @Test
    public void testCreateTempFileWithSettings() throws IOException {
        fileManager = container.get(IFileManager.class);
        try (IFile file = fileManager.createTempFile()) {
            String pathStr = file.getPathStr();
            Assert.assertTrue(pathStr.contains("/WorkingDirectory/"));
        }
    }
}
