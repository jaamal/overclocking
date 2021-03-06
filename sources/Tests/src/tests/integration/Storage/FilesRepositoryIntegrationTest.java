package tests.integration.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.filesRepository.CassandraFilesRepository;
import storage.filesRepository.IFilesRepository;
import tests.integration.StorageTestBase;
import dataContracts.ContentType;
import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class FilesRepositoryIntegrationTest extends StorageTestBase
{
    private CassandraFilesRepository cassandraFilesRepository;
    private ISchemeInitializer schemeInitializer;

    @Override
    public void setUp()
    {
        super.setUp();
        cassandraFilesRepository = (CassandraFilesRepository) container.get(IFilesRepository.class);
        schemeInitializer = container.get(ISchemeInitializer.class);
        schemeInitializer.setUpCluster();
    }
    
    @Override
    public void tearDown() {
        schemeInitializer.truncateKeyspace(KeySpaces.files.name());
        super.tearDown();
    }

    @Test
    public void testGetFileStream() throws IOException
    {        
        byte[] expected = new byte[]{ 1, 2, 3, 4, 5, 6, 7 };

        FileMetadata fileMeta = new FileMetadata("fileId", "testfile", 0, FileType.Text, ContentType.PlainText);
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 1, new byte[]{1, 2}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 2, new byte[]{3, 4}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 3, new byte[]{5, 6}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 4, new byte[]{7}));
        
        InputStream in = cassandraFilesRepository.getFileStream(fileMeta);
        byte[] actual = new byte[expected.length];
        int pos = 0;
        int b;
        while ((b = in.read()) != -1)
            actual[pos++] = (byte) b;
        Assert.assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetFileIterator() {
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 1, new byte[]{1, 2}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 2, new byte[]{3, 4}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 3, new byte[]{5, 6}));
        cassandraFilesRepository.saveBatch(new FileBatch("fileId", 4, new byte[]{7}));
        
        for (FileBatch fileBatch : cassandraFilesRepository.getFileIterator("fileId")) {
            switch (fileBatch.batchNumber) {
            case 1:
                Assert.assertArrayEquals(new byte[] {1, 2}, fileBatch.batchData);
                break;
            case 2: 
                Assert.assertArrayEquals(new byte[] {3, 4}, fileBatch.batchData);
                break;
            case 3: 
                Assert.assertArrayEquals(new byte[] {5, 6}, fileBatch.batchData);
                break;
            case 4: 
                Assert.assertArrayEquals(new byte[] {7}, fileBatch.batchData);
                break;
            default:
                throw new RuntimeException(String.format("Unexpected batch with number %s.", fileBatch.batchNumber));
            }
        }
    }
    
    @Test
    public void testSaveAndGetMeta()
    {        
        final FileMetadata fileMeta1 = new FileMetadata("fileId1", "fileName1", 1024, FileType.Unspecified, ContentType.PlainText);
        final FileMetadata fileMeta2 = new FileMetadata("fileId2", "fileName2", 1024, FileType.Unspecified, ContentType.PlainText);

        cassandraFilesRepository.saveMeta(fileMeta1);
        cassandraFilesRepository.saveMeta(fileMeta2);

        
        FileMetadata actual1 = cassandraFilesRepository.getMeta("fileId1");
        FileMetadata actual2 = cassandraFilesRepository.getMeta("fileId2");
        Assert.assertEquals(fileMeta1, actual1);
        Assert.assertEquals(fileMeta2, actual2);
    }

    @Test
    public void putMetaData( ){
        cassandraFilesRepository.saveMeta(new FileMetadata("fileId1", "fileName1", 1024, FileType.Unspecified, ContentType.PlainText));
    }
    
    @Test
    public void testFileIds() {
        final FileMetadata fileMeta1 = new FileMetadata("fileId1", "fileName1", 1024, FileType.Unspecified, ContentType.PlainText);
        final FileMetadata fileMeta2 = new FileMetadata("fileId2", "fileName2", 1024, FileType.Unspecified, ContentType.PlainText);
        final FileMetadata fileMeta3 = new FileMetadata("fileId3", "fileName2", 1024, FileType.Unspecified, ContentType.PlainText);

        cassandraFilesRepository.saveMeta(fileMeta1);
        cassandraFilesRepository.saveMeta(fileMeta2);
        cassandraFilesRepository.saveMeta(fileMeta3);
        
        String[] actuals = cassandraFilesRepository.getFileIds(0, 10).toArray(new String[0]);
        Arrays.sort(actuals);
        Assert.assertArrayEquals(new String[] {"fileId1", "fileId2", "fileId3"}, actuals);
        
        actuals = cassandraFilesRepository.getFileIds(0, 2).toArray(new String[0]);
        Assert.assertEquals(2, actuals.length);
        
        actuals = cassandraFilesRepository.getFileIds(1, 3).toArray(new String[0]);
        Assert.assertEquals(2, actuals.length);
    }
}
