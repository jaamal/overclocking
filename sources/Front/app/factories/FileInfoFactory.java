package factories;

import dataContracts.files.FileMetadata;
import models.FileInfo;

public class FileInfoFactory implements IFileInfoFactory {
    @Override
    public FileInfo create(FileMetadata fileMetadata) {
        return new FileInfo(fileMetadata.getId(), fileMetadata.getName(), fileMetadata.getSize(), fileMetadata.getType().toString());
    }
}
