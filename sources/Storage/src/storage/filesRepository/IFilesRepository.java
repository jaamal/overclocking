package storage.filesRepository;

import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;

import java.io.InputStream;
import java.util.Iterator;

public interface IFilesRepository
{
    void saveMeta(FileMetadata fileMeta);
    FileMetadata getMetadata(String fileId);
    FileMetadata[] getAllFiles();
    String[] getAllIds();
    
    void saveBatch(FileBatch fileBatch);
    FileBatch getBatch(String fileId, int batchNumber);
    Iterator<FileBatch> getFile(String fileId);
    InputStream getFileStream(FileMetadata fileMeta);

    void remove(String fileId);
}
