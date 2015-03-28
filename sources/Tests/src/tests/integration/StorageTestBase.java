package tests.integration;

import storage.filesRepository.IFilesRepository;
import caching.configuration.ICacheConfiguration;
import commons.files.IFileManager;

public class StorageTestBase extends IntegrationTestBase
{
    protected IFilesRepository filesRepository;
    protected IFileManager fileManager;
    
    private ICacheConfiguration cacheConfiguration = new ICacheConfiguration(){
        @Override
        public int getCacheLineCount()
        {
            return 1000;
        }

        @Override
        public int getCacheLineLength()
        {
            return 1000;
        }
    };

    @Override
    public void setUp()
    {
        super.setUp();
        
        container.bindInstance(ICacheConfiguration.class, cacheConfiguration);
        
        filesRepository = container.get(IFilesRepository.class);
        fileManager = container.get(IFileManager.class);
    }
}
