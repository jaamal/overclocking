package compressingCore.dataFiltering;

import java.io.Reader;
import java.nio.file.Path;

import dataContracts.files.FileType;

public interface IFileFilter {
    Path apply(FileType fileType, Reader reader);
}
