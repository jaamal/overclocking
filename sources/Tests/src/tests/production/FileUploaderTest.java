package tests.production;

import commons.files.IFile;
import commons.files.IFileManager;
import compressionservice.upload.IFileUploader;
import dataContracts.ContentType;
import dataContracts.files.FileType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileUploaderTest extends ProductionTestBase {

    @Test
    public void LoadDnaFiles() throws IOException {
        String filePath = "C:\\programming\\overclocking\\DNA\\AAMG.gz";
        IFileManager fileManager = container.get(IFileManager.class);
        IFileUploader fileUploader = container.get(IFileUploader.class);

        IFile file = fileManager.getFile(filePath);
        FileType fileType = FileType.Dna;
        ContentType contentType = ContentType.GZip;
        System.out.printf("Loading %s %s %s", file.getPathStr(), fileType, contentType);
        fileUploader.upload(file, fileType, contentType);
        System.out.println("Done");
    }

    @Test
    public void LoadRandomFiles() throws IOException {
        File directory = new File("D:\\overclocking\\Random");
        for (File windowsFile : directory.listFiles()) {
            IFileManager fileManager = container.get(IFileManager.class);
            IFileUploader fileUploader = container.get(IFileUploader.class);

            IFile file = fileManager.getFile(windowsFile.getAbsolutePath());
            FileType fileType = FileType.Text;
            ContentType contentType = ContentType.PlainText;
            System.out.printf("Loading %s %s %s", file.getPathStr(), fileType, contentType);
            fileUploader.upload(file, fileType, contentType);
            System.out.println("Done");
        }
    }

    @Test
    public void LoadBadFiles() throws IOException {
        File directory = new File("D:\\overclocking\\Bad_cut");
        for (File windowsFile : directory.listFiles()) {
            IFileManager fileManager = container.get(IFileManager.class);
            IFileUploader fileUploader = container.get(IFileUploader.class);

            IFile file = fileManager.getFile(windowsFile.getAbsolutePath());
            FileType fileType = FileType.Text;
            ContentType contentType = ContentType.PlainText;
            System.out.printf("Loading %s %s %s", file.getPathStr(), fileType, contentType);
            fileUploader.upload(file, fileType, contentType);
            System.out.println("Done");
        }
    }
}
