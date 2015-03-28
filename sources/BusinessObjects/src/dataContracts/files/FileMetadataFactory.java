package dataContracts.files;

import dataContracts.ContentType;

public class FileMetadataFactory implements IFileMetadataFactory
{
    public FileMetadata create(String fileId, String fileName, long fileSize, FileType fileType, ContentType contentType)
    {
        return new FileMetadata(fileId, fileName, fileSize, fileType, contentType);
    }
}
