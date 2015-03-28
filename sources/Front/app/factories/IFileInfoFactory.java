package factories;

import dataContracts.files.FileMetadata;
import models.FileInfo;

public interface IFileInfoFactory {
    FileInfo create(FileMetadata fileMetadata);
}
