package compressionservice.upload;

import commons.files.IFile;
import dataContracts.ContentType;
import dataContracts.files.FileType;

public interface IFileUploader {
    void upload(IFile file, FileType fileType, ContentType contentType);
}
