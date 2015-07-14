package tests.integration.fileUploader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.filesRepository.IFilesRepository;
import tests.integration.StorageTestBase;

import commons.files.IFile;
import commons.files.IFileManager;
import compressionservice.upload.IFileUploader;

import dataContracts.ContentType;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class FileLoaderIntegrationTest extends StorageTestBase
{
    private IFilesRepository filesRepository;
    private IFileManager fileManager;
    private ISchemeInitializer schemeInitializer;

    @Override
    public void setUp()
    {
        super.setUp();
        filesRepository = container.get(IFilesRepository.class);
        fileManager = container.get(IFileManager.class);
        schemeInitializer = container.get(ISchemeInitializer.class);
        schemeInitializer.setUpCluster();
    }
    
    @Override
    public void tearDown() {
        schemeInitializer.truncateKeyspace(KeySpaces.files.name());
        super.tearDown();
    }
    
    @Test
    public void testUpload() throws IOException
    {
        final String path = Paths.get("testFiles", "file_for_loading.txt").toString();
        final byte[] expected = readFile(path);
        final IFile file = fileManager.getFile(path);
        
        IFileUploader fileUploader = container.get(IFileUploader.class);
        String fileId = fileUploader.upload(file, FileType.Unspecified, ContentType.PlainText);

        FileMetadata fileMetadata = filesRepository.getMeta(fileId);
        Assert.assertNotNull(fileMetadata);
        Assert.assertEquals(fileMetadata.getId(), file.getPathStr());
        Assert.assertEquals(fileMetadata.getName(), file.getPathStr());
        Assert.assertEquals(fileMetadata.getSize(), file.size());
        Assert.assertEquals(fileMetadata.getType(), FileType.Unspecified);

        final InputStream stream = filesRepository.getFileStream(fileMetadata);
        final byte[] actual = new byte[expected.length];
        int cp;
        int pos = 0;
        while ((cp = stream.read()) != -1)
            actual[pos++] = (byte) cp;

        Assert.assertArrayEquals(expected, actual);
    }

    private byte[] readFile(String path) throws IOException
    {
        final IFile file = fileManager.getFile(path);
        final int size = (int) file.size();
        byte[] body = new byte[size];
        file.read(0, body);
        return body;
    }
}
