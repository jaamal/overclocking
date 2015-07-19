package data.filters;

import java.io.Reader;
import java.nio.file.Path;

import dataContracts.files.FileType;

public interface ITypedFileFilter {
    FileType getFileType();
    Path apply(Reader reader);
}
