package storage.filesRepository;

import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface IFilesRepository
{
    void saveMeta(FileMetadata fileMeta);
    FileMetadata getMeta(String fileId);
    FileMetadata[] getMeta(Collection<String> fileIds);
    @Deprecated
    List<String> getFileIds();
    List<String> getFileIds(int from, int count);
    
    void saveBatch(FileBatch fileBatch);
    FileBatch getBatch(String fileId, int batchNumber);
    Iterator<FileBatch> getFile(String fileId);
    InputStream getFileStream(FileMetadata fileMeta);

    void remove(String fileId);
}
