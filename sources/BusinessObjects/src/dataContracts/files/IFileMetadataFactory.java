package dataContracts.files;

import dataContracts.ContentType;

public interface IFileMetadataFactory
{
    FileMetadata create(String fileId, String fileName, long fileSize, FileType fileType, ContentType contentType);
}
