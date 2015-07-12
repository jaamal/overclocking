package tests.integration;

import storage.filesRepository.IFilesRepository;

import commons.files.IFileManager;

public class StorageTestBase extends IntegrationTestBase
{
    protected IFilesRepository filesRepository;
    protected IFileManager fileManager;

    @Override
    public void setUp()
    {
        super.setUp();
        
        filesRepository = container.get(IFilesRepository.class);
        fileManager = container.get(IFileManager.class);
    }
}
