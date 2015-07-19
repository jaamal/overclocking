package data.filters;

import java.io.Reader;
import java.nio.file.Path;

import dataContracts.files.FileType;

public class FileFilter implements IFileFilter {

    private ITypedFileFilter[] filters;

    public FileFilter(ITypedFileFilter[] filters) {
        this.filters = filters;
    }
    
    @Override
    public Path apply(FileType fileType, Reader reader) {
        return chooseFilterOrDie(fileType).apply(reader);
    }
    
    private ITypedFileFilter chooseFilterOrDie(FileType fileType) {
        for (ITypedFileFilter filter : filters) {
            if (filter.getFileType() != fileType)
                continue;
            return filter;
        }
        throw new RuntimeException(String.format("File filter for content type %s was not found.", fileType.name()));
    }
}
