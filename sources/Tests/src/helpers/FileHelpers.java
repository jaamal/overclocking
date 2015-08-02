package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import overclocking.jrobocontainer.container.IContainer;
import storage.filesRepository.IFilesRepository;
import commons.files.FileManager;
import commons.files.IFile;
import commons.files.IFileManager;
import data.filters.IFileFilter;
import dataContracts.ContentType;
import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class FileHelpers
{
    private static String testFilesDir = null;
    private static IFileManager fileManager = new FileManager(null);

    public static String getAbsolutePathToTestFile(String localPath) {
        if (testFilesDir == null) {
            String baseDir = new File("").getAbsolutePath();
            testFilesDir = baseDir.contains("Tests")
                    ? Paths.get(baseDir, "testFiles").toString()
                    : Paths.get(baseDir, "Tests", "testFiles").toString();
        }

        return Paths.get(testFilesDir, localPath).toString();
    }
    
    public static String readDNA(IContainer container, String localPath) {
        try (Reader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(getAbsolutePathToTestFile(localPath)))))) {
            Path pathToLocalFile = container.get(IFileFilter.class).apply(FileType.Dna, reader);
            try (Reader localReader = new BufferedReader(new InputStreamReader(new FileInputStream( pathToLocalFile.toAbsolutePath().toString())))) {
                return IOUtils.toString(localReader);
            }
            catch(Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readTestFile(String localPath) {
        IFile file = fileManager.getFile(getAbsolutePathToTestFile(localPath));
        int size = (int) file.size();
        byte[] result = new byte[size];
        try {
            file.read(0, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String readTestFile(String localPath, Charset charset) {
        byte[] fileContent = readTestFile(localPath);
        return new String(fileContent, charset);
    }
    
    public static FileMetadata writeDnaToRepository(String fileName, ContentType contentType, IFilesRepository repo) {
        return writeFileToRepository(fileName, FileType.Dna, contentType, repo);
    }
    
    public static FileMetadata writeFileToRepository(String fileName, FileType fileType, ContentType contentType, IFilesRepository repo)
    {
        try (IFile file = fileManager.getFile(Paths.get("testFiles", fileName).toString()))
        {
            FileMetadata fileMetadata = new FileMetadata(UUID.randomUUID().toString(), fileName, file.size(), fileType, contentType);
            System.out.println(String.format("Write file %s with id %s and size %s.", fileName, fileMetadata.getId(), file.size()));
            repo.saveMeta(fileMetadata);
            final int batchCount = 10;
            final int batchSize = (int) (file.size() / batchCount);
            byte[] buffer = new byte[batchSize];
            int batchNumber = 0;
            while (true)
            {
                byte[] actualBatchData = buffer;
                int actual = file.read(batchNumber * batchSize, actualBatchData);
                if (actual < 0)
                    break;
                if (actual < batchSize)
                {
                    byte[] actualData = new byte[actual];
                    System.arraycopy(actualBatchData, 0, actualData, 0, actual);
                    actualBatchData = actualData;
                }
                repo.saveBatch(new FileBatch(fileMetadata.getId(), batchNumber, actualBatchData));
                batchNumber++;
            }
            return fileMetadata;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
