package tests.integration.Caching;

import org.junit.Test;

import caching.connections.ITemporaryFileFactory;
import tests.integration.IntegrationTestBase;

public class TemporaryFileFactoryTest extends IntegrationTestBase
{
    private ITemporaryFileFactory temporaryFileFactory;

    @Override
    public void setUp() {
        super.setUp();
        
        temporaryFileFactory = container.get(ITemporaryFileFactory.class);
    }
    
    @Test
    public void testCreateTempFile() {
        temporaryFileFactory.getTemporaryFile();
    }
}
