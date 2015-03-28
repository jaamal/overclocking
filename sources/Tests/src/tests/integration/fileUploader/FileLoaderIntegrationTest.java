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
import commons.files.FileManager;
import commons.files.IFile;
import compressionservice.upload.IFileUploader;
import dataContracts.ContentType;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class FileLoaderIntegrationTest extends StorageTestBase
{
    private IFilesRepository cassandraFilesRepository;
    private FileManager fileManager = new FileManager();
    private ISchemeInitializer schemeInitializer;

    @Override
    public void setUp()
    {
        super.setUp();
        cassandraFilesRepository = container.get(IFilesRepository.class);
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
        fileUploader.upload(file, FileType.Unspecified, ContentType.PlainText);

        FileMetadata fileMetadata = findMetadataByFileId(cassandraFilesRepository.getAllFiles(), file.getFileName());
        Assert.assertNotNull(fileMetadata);
        Assert.assertEquals(fileMetadata.getId(), file.getFileName());
        Assert.assertEquals(fileMetadata.getFileName(), file.getFileName());
        Assert.assertEquals(fileMetadata.getFileSize(), file.size());
        Assert.assertEquals(fileMetadata.getFileType(), FileType.Unspecified);

        final InputStream stream = cassandraFilesRepository.getFileStream(fileMetadata);
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

    private static FileMetadata findMetadataByFileId(FileMetadata[] fileMetadatas, String fileId)
    {
        for (FileMetadata fileMetadata : fileMetadatas)
            if (fileMetadata.getId().equals(fileId))
                return fileMetadata;
        return null;
    }

}
