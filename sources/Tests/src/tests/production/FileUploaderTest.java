package tests.production;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import commons.files.IFile;
import commons.files.IFileManager;
import compressionservice.upload.IFileUploader;
import dataContracts.ContentType;
import dataContracts.files.FileType;
import storage.cassandraClient.ISchemeInitializer;

public class FileUploaderTest extends ProductionTestBase {

    @Override
    public void setUp()
    {
        super.setUp();
        
        ISchemeInitializer schemaInitializer = container.get(ISchemeInitializer.class);
        schemaInitializer.setUpCluster();
    }

    @Test
    public void UploadDnaFiles() throws IOException {
        UploadFiles(FilesConsts.wgsFolderPath, FileType.Dna, ContentType.GZip);
    }

    @Test
    public void UploadRandomFiles() throws IOException {
        UploadFiles(FilesConsts.randomFolderPath, FileType.Text, ContentType.PlainText);
    }
    
    private void UploadFiles(String folderPath, FileType fileType, ContentType contentType) {
        IFileManager fileManager = container.get(IFileManager.class);
        IFileUploader fileUploader = container.get(IFileUploader.class);
        
        File filesDir = new File(folderPath);
        File[] files = filesDir.listFiles();
        
        if (files == null)
            throw new RuntimeException(String.format("Files are not found at %s.", folderPath));
        
        for (File file : files) {
            IFile _file = fileManager.getFile(file.getAbsolutePath());
            System.out.println(String.format("Uploading file %s.", _file.getPathStr()));
            fileUploader.upload(_file, fileType, contentType);
            System.out.println(String.format("File %s uploaded.", _file.getPathStr()));
        }
    }
}
