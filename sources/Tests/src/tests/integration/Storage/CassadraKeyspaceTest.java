package tests.integration.Storage;

import org.junit.Assert;
import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.CFValidationClass;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import storage.cassandraClient.IEntityHandler;
import storage.cassandraClient.ISchemeInitializer;
import tests.integration.StorageTestBase;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

import dataContracts.ContentType;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class CassadraKeyspaceTest extends StorageTestBase {
    
    private ICassandraConnectionFactory cassandraConnectionFactory;
    private ISchemeInitializer schemeInitializer;
    private IEntityHandler entityHandler;

    @Override
    public void setUp() {
        super.setUp();
        
        cassandraConnectionFactory = container.get(ICassandraConnectionFactory.class);
        schemeInitializer = container.get(ISchemeInitializer.class);
        entityHandler = container.get(IEntityHandler.class);
    }
    
    @Test
    public void testConnectToNonexistentKeyspace() throws Exception {
        Assert.assertFalse(schemeInitializer.isKeyspaceExists("zzzspace"));
    }

    @Test
    public void testCreateKeyspace() throws Exception {
        if (!schemeInitializer.isKeyspaceExists("testspace")){
            schemeInitializer.createKeyspace("testspace", 1);
        }
        Assert.assertTrue(schemeInitializer.isKeyspaceExists("testspace"));
    }
    
    @Test
    public void testConnectToNonexistentColumnFamily() throws Exception {
        Keyspace keyspace;
        if (!schemeInitializer.isKeyspaceExists("testspace")){
            keyspace = schemeInitializer.createKeyspace("testspace", 1);
        }
        else {
            keyspace = cassandraConnectionFactory.getKeyspace("testspace");
        }
        
        Assert.assertFalse(schemeInitializer.isColumnFamilyExisits(keyspace, ColumnFamily.newColumnFamily("nonexistent", StringSerializer.get(), StringSerializer.get())));
    }
    
    @Test
    public void testWriteAndReadEntity() {
        Keyspace keyspace;
        if (!schemeInitializer.isKeyspaceExists("files")){
            keyspace = schemeInitializer.createKeyspace("files", 1);
        }
        else {
            keyspace = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name());
        }
        
        if (!schemeInitializer.isColumnFamilyExisits(keyspace, ColumnFamilies.FileMetas)){
            schemeInitializer.createColumnFamily(keyspace, ColumnFamilies.FileMetas, CFValidationClass.BytesType);
        }
        
        if (!schemeInitializer.isColumnFamilyExisits(keyspace, ColumnFamilies.FileDatas)){
            schemeInitializer.createColumnFamily(keyspace, ColumnFamilies.FileDatas, CFValidationClass.BytesType);
        }
        
        FileMetadata meta = new FileMetadata("2222", "zzzz", 1010, FileType.Dna, ContentType.PlainText);
        entityHandler.write(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta);
        FileMetadata actual = entityHandler.read(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta.getId());
        
        Assert.assertEquals(meta.getId(), actual.getId());
        Assert.assertEquals(meta.getName(), actual.getName());
    }
    
    @Test
    public void testWriteAndDeleteEntity() {
        Keyspace keyspace;
        if (!schemeInitializer.isKeyspaceExists("files")){
            keyspace = schemeInitializer.createKeyspace("files", 1);
        }
        else {
            keyspace = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name());
        }
        
        if (!schemeInitializer.isColumnFamilyExisits(keyspace, ColumnFamilies.FileMetas)){
            schemeInitializer.createColumnFamily(keyspace, ColumnFamilies.FileMetas, CFValidationClass.BytesType);
        }
        
        if (!schemeInitializer.isColumnFamilyExisits(keyspace, ColumnFamilies.FileDatas)){
            schemeInitializer.createColumnFamily(keyspace, ColumnFamilies.FileDatas, CFValidationClass.BytesType);
        }
        
        FileMetadata meta = new FileMetadata("8477", "zzz", 1210, FileType.Dna, ContentType.PlainText);
        entityHandler.write(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta);
        FileMetadata actual = entityHandler.read(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta.getId());
        Assert.assertNotNull(actual);
        
        entityHandler.delete(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta.getId());
        actual = entityHandler.read(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, meta.getId());
        Assert.assertNull(actual);
    }
}
